package org.noah.web.base.config.swagger;


import io.swagger.models.parameters.Parameter;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2MapperImpl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * 重写 将Document转换成Swagger 类, 根据order进行排序
 * 同一个接口，可能会有几种不同的实现类，而默认只会采取其中一种的情况下 @Primary
 */
@Primary
@Component("ServiceModelToSwagger2Mapper")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomModelToSwaggerMapper extends ServiceModelToSwagger2MapperImpl {

    @Override
    protected List<Parameter> parameterListToParameterList(List<springfox.documentation.service.Parameter> list) {
        //list需要根据order|postion排序
        list = list.stream().sorted(Comparator.comparingInt(springfox.documentation.service.Parameter::getOrder)).collect(Collectors.toList());
        return super.parameterListToParameterList(list);
    }
}
