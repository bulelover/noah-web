package org.noah.web.base.config.swagger;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.members.ResolvedField;
import com.fasterxml.classmate.members.ResolvedMember;
import com.fasterxml.classmate.members.ResolvedMethod;
import org.noah.core.annotation.IgnoreSwaggerParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.Maps;
import springfox.documentation.schema.Types;
import springfox.documentation.schema.property.bean.AccessorsProvider;
import springfox.documentation.schema.property.field.FieldProvider;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.schema.AlternateTypeProvider;
import springfox.documentation.spi.schema.EnumTypeDeterminer;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.parameter.ExpansionContext;
import springfox.documentation.spring.web.readers.parameter.ModelAttributeField;
import springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander;
import springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterMetadataAccessor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.noah.web.base.config.swagger.ParameterTypeDeterminer.determineScalarParameterType;
import static org.springframework.util.StringUtils.isEmpty;
import static springfox.documentation.schema.Collections.collectionElementType;
import static springfox.documentation.schema.Collections.isContainerType;
import static springfox.documentation.schema.Types.isVoid;
import static springfox.documentation.schema.Types.typeNameFor;

/**
 * 添加自定义忽略字段注解，可忽略包括对象，集合的任意字段
 */
@Component
@Primary
public class CustomizeModelAttributeParameterExpander extends ModelAttributeParameterExpander {
    private static final Logger LOG = LoggerFactory.getLogger(ModelAttributeParameterExpander.class);
    private final FieldProvider fields;
    private final AccessorsProvider accessors;
    private final EnumTypeDeterminer enumTypeDeterminer;

    private DocumentationPluginsManager pluginsManager;

    @Autowired
    public CustomizeModelAttributeParameterExpander(
            FieldProvider fields,
            AccessorsProvider accessors,
            EnumTypeDeterminer enumTypeDeterminer, DocumentationPluginsManager pluginsManager) {
        super(fields, accessors, enumTypeDeterminer);
        this.fields = fields;
        this.accessors = accessors;
        this.enumTypeDeterminer = enumTypeDeterminer;
        this.pluginsManager = pluginsManager;
    }

    public List<Parameter> expand(ExpansionContext context) {
        List<Parameter> parameters = new ArrayList<>();
        Set<PropertyDescriptor> propertyDescriptors = propertyDescriptors(context.getParamType().getErasedType());
        Map<Method, PropertyDescriptor> propertyLookupByGetter
                = propertyDescriptorsByMethod(context.getParamType().getErasedType(), propertyDescriptors);
        Iterable<ResolvedMethod> getters = accessors.in(context.getParamType()).stream()
                .filter(onlyValidGetters(propertyLookupByGetter.keySet())).collect(toList());

        Map<String, ResolvedField> fieldsByName =
                StreamSupport.stream(this.fields.in(context.getParamType()).spliterator(), false)
                        .collect(toMap((ResolvedMember::getName), identity()));


        LOG.debug("Expanding parameter type: {}", context.getParamType());
        final AlternateTypeProvider alternateTypeProvider = context.getDocumentationContext().getAlternateTypeProvider();

        List<ModelAttributeField> attributes =
                allModelAttributes(
                        propertyLookupByGetter,
                        getters,
                        fieldsByName,
                        alternateTypeProvider);

        attributes.stream()
                .filter(simpleType().negate())
                .filter(recursiveType(context).negate())
                .forEach((each) -> {
                    LOG.debug("Attempting to expand expandable property: {}", each.getName());
                    parameters.addAll(
                            expand(
                                    context.childContext(
                                            nestedParentName(context.getParentName(), each),
                                            each.getFieldType(),
                                            context.getOperationContext())));
                });

        Stream<ModelAttributeField> collectionTypes = attributes.stream()
                .filter(isCollection().and(recursiveCollectionItemType(context.getParamType()).negate()));
        collectionTypes.forEachOrdered((each) -> {
            LOG.debug("Attempting to expand collection/array field: {}", each.getName());

            ResolvedType itemType = collectionElementType(each.getFieldType());
            if (Types.isBaseType(itemType) || enumTypeDeterminer.isEnum(itemType.getErasedType())) {
                parameters.add(simpleFields(context.getParentName(), context, each));
            } else {
                ExpansionContext childContext = context.childContext(
                        nestedParentName(context.getParentName(), each),
                        itemType,
                        context.getOperationContext());
                if (!context.hasSeenType(itemType)) {
                    parameters.addAll(expand(childContext));
                }
            }
        });

        Stream<ModelAttributeField> simpleFields = attributes.stream().filter(simpleType());
        simpleFields.forEach((each) -> {
            parameters.add(simpleFields(context.getParentName(), context, each));
        });
        return parameters.stream()
                .filter(((Predicate<Parameter>) Parameter::isHidden).negate())
                .filter(voidParameters().negate())
                .collect(toList());
    }

    private List<ModelAttributeField> allModelAttributes(
            Map<Method, PropertyDescriptor> propertyLookupByGetter,
            Iterable<ResolvedMethod> getters,
            Map<String, ResolvedField> fieldsByName,
            AlternateTypeProvider alternateTypeProvider) {

        Stream<ModelAttributeField> modelAttributesFromGetters = StreamSupport.stream(getters.spliterator(), false)
                .map(toModelAttributeField(fieldsByName, propertyLookupByGetter, alternateTypeProvider));

        Stream<ModelAttributeField> modelAttributesFromFields = fieldsByName.values().stream()
                .filter(ResolvedMember::isPublic)
                .map(toModelAttributeField(alternateTypeProvider));

        return Stream.concat(
                modelAttributesFromFields,
                modelAttributesFromGetters)
                .collect(toList());
    }

    private Function<ResolvedField, ModelAttributeField> toModelAttributeField(
            final AlternateTypeProvider alternateTypeProvider) {

        return input -> new ModelAttributeField(
                alternateTypeProvider.alternateFor(input.getType()),
                input.getName(),
                input,
                input);
    }

    private Predicate<Parameter> voidParameters() {
        return input -> isVoid(Objects.requireNonNull(input.getType().orElse(null)));
    }

    private Predicate<ModelAttributeField> recursiveCollectionItemType(final ResolvedType paramType) {
        return input -> Objects.equals(collectionElementType(input.getFieldType()), paramType);
    }

    private Parameter simpleFields(
            String parentName,
            ExpansionContext context,
            ModelAttributeField each) {
        LOG.debug("Attempting to expand field: {}", each);
        String dataTypeName = ofNullable(typeNameFor(each.getFieldType().getErasedType()))
                .orElse(each.getFieldType().getErasedType().getSimpleName());
        LOG.debug("Building parameter for field: {}, with type: ", each, each.getFieldType());
        ParameterExpansionContext parameterExpansionContext = new ParameterExpansionContext(
                dataTypeName,
                parentName,
                determineScalarParameterType(
                        context.getOperationContext().consumes(),
                        context.getOperationContext().httpMethod()),
                new ModelAttributeParameterMetadataAccessor(
                        each.annotatedElements(),
                        each.getFieldType(),
                        each.getName()),
                context.getDocumentationContext().getDocumentationType(),
                new ParameterBuilder());
        return pluginsManager.expandParameter(parameterExpansionContext);
    }

    private Predicate<ModelAttributeField> recursiveType(final ExpansionContext context) {
        return input -> context.hasSeenType(input.getFieldType());
    }

    private Predicate<ModelAttributeField> simpleType() {
        return isCollection().negate().and(isMap().negate())
                .and(
                        belongsToJavaPackage()
                                .or(isBaseType())
                                .or(isEnum()));
    }

    private Predicate<ModelAttributeField> isCollection() {
        return input -> isContainerType(input.getFieldType());
    }

    private Predicate<ModelAttributeField> isMap() {
        return input -> Maps.isMapType(input.getFieldType());
    }

    private Predicate<ModelAttributeField> isEnum() {
        return input -> enumTypeDeterminer.isEnum(input.getFieldType().getErasedType());
    }

    private Predicate<ModelAttributeField> belongsToJavaPackage() {
        return input -> ClassUtils.getPackageName(input.getFieldType().getErasedType()).startsWith("java.lang");
    }

    private Predicate<ModelAttributeField> isBaseType() {
        return input -> Types.isBaseType(input.getFieldType())
                || input.getFieldType().isPrimitive();
    }

    private Function<ResolvedMethod, ModelAttributeField> toModelAttributeField(
            final Map<String, ResolvedField> fieldsByName,
            final Map<Method, PropertyDescriptor> propertyLookupByGetter,
            final AlternateTypeProvider alternateTypeProvider) {
        return input -> {
            String name = propertyLookupByGetter.get(input.getRawMember()).getName();
            return new ModelAttributeField(
                    fieldType(alternateTypeProvider, input),
                    name,
                    input,
                    fieldsByName.get(name));
        };
    }

    private Predicate<ResolvedMethod> onlyValidGetters(final Set<Method> methods) {
        return input -> methods.contains(input.getRawMember());
    }

    private String nestedParentName(String parentName, ModelAttributeField attribute) {
        String name = attribute.getName();
        ResolvedType fieldType = attribute.getFieldType();
        if (isContainerType(fieldType) && !Types.isBaseType(collectionElementType(fieldType))) {
            name += "[0]";
        }

        if (isEmpty(parentName)) {
            return name;
        }
        return String.format("%s.%s", parentName, name);
    }

    private ResolvedType fieldType(AlternateTypeProvider alternateTypeProvider, ResolvedMethod method) {
        return alternateTypeProvider.alternateFor(method.getType());
    }

    /**
     * 获取包含父级的所有字段
     */
    private Field getAllDeclaredField(Class<?> clazz, String name){
        Field field = null;
        try {
            field = clazz.getDeclaredField(name);
        } catch (Exception e) {
            try {
                //没有的话再往父级找一下
                Class<?> superClazz = clazz.getSuperclass();
                if (superClazz == null) {
                    return null;
                }
                field = getAllDeclaredField(superClazz, name);
            }catch (Exception e2){
                LOG.debug(String.format("Failed to get bean properties on (%s)", clazz), e2);

            }
        }
        return field;
    }

    private Set<PropertyDescriptor> propertyDescriptors(final Class<?> clazz) {
        try {
            Set<PropertyDescriptor> beanProps = new HashSet<>();
            PropertyDescriptor[] descriptors = getBeanInfo(clazz).getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors) {
                // 增加逻辑，忽略@IgnoreSwaggerParameter注解的字段
                Field field = getAllDeclaredField(clazz, descriptor.getName());
                if (field != null) {
                    field.setAccessible(true);
                    IgnoreSwaggerParameter ignoreSwaggerParameter = field.getDeclaredAnnotation(IgnoreSwaggerParameter.class);
                    if (ignoreSwaggerParameter != null) {
                        continue;
                    }
                }
                // 增加结束
                if (descriptor.getReadMethod() != null) {
                    beanProps.add(descriptor);
                }
            }
            return beanProps;
        } catch (IntrospectionException e) {
            LOG.warn(String.format("Failed to get bean properties on (%s)", clazz), e);
        }
        return emptySet();
    }

    private Map<Method, PropertyDescriptor> propertyDescriptorsByMethod(
            final Class<?> clazz,
            Set<PropertyDescriptor> propertyDescriptors) {
        return propertyDescriptors.stream()
                .filter(input -> input.getReadMethod() != null
                        && !clazz.isAssignableFrom(Collection.class)
                        && !"isEmpty".equals(input.getReadMethod().getName()))
                .collect(toMap(PropertyDescriptor::getReadMethod, identity()));

    }

    BeanInfo getBeanInfo(Class<?> clazz) throws IntrospectionException {
        return Introspector.getBeanInfo(clazz);
    }

    public DocumentationPluginsManager getPluginsManager() {
        return pluginsManager;
    }

    public void setPluginsManager(DocumentationPluginsManager pluginsManager) {
        this.pluginsManager = pluginsManager;
    }
}
