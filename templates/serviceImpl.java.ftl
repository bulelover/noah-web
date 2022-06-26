package ${package.ServiceImpl};

import org.noah.core.common.BaseServiceImpl;
import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends BaseServiceImpl<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Override
    protected String getOrderTableAlias(String column, Boolean isColumn) {
        if(isColumn){
            return "t."+column;
        }
        return null;
    }

}
</#if>
