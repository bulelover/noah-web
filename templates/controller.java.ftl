package ${package.Controller};

<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.noah.core.annotation.Log;
import org.noah.core.common.BaseResult;
import org.noah.core.common.PageResult;
import org.noah.core.utils.BeanUtils;
import org.noah.core.utils.CheckUtils;
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import org.springframework.beans.factory.annotation.Autowired;
import ${package.Entity}.${entity}ListVO;
import ${package.Entity}.${entity}VO;
import ${package.Entity}.${entity}Page;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Api(tags = "${apiTag!}")
@ApiSort(${apiSort!})
@Log("${logPrefix!}")
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("${requestMapping!}")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    private final ${table.serviceName} ${entity?uncap_first}Service;

    @Autowired
    public ${table.controllerName}(${table.serviceName} ${entity?uncap_first}Service) {
        this.${entity?uncap_first}Service = ${entity?uncap_first}Service;
    }

    @SaCheckPermission("${permission!}-view")
    @GetMapping("/getById")
    @ApiImplicitParam(name = "id", value = "${tableDesc!}ID")
    @ApiOperation(value = "根据ID查询${tableDesc!}")
    @ApiOperationSupport(order = 10)
    public BaseResult<${entity}ListVO> getById(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        ${entity} entity = ${entity?uncap_first}Service.getById(id);
        if(entity == null || "0".equals(entity.getFlag())){
            return this.failure("信息不存在");
        }
        return this.success(BeanUtils.parse(entity, ${entity}ListVO.class));
    }

    @SaCheckPermission("${permission!}")
    @PostMapping("/page")
    @ApiOperation(value = "${tableDesc!}分页列表查询")
    @ApiOperationSupport(order = 20)
    public BaseResult<PageResult<${entity}ListVO>> page(${entity}Page page){
        PageResult<${entity}ListVO> res = ${entity?uncap_first}Service.selectPage(page,  ${entity}ListVO.class);
        return this.success(res);
    }

    @SaCheckPermission("${permission!}-add")
    @Log("新增")
    @ApiOperation(value = "新增${tableDesc!}信息")
    @ApiOperationSupport(order = 30, ignoreParameters = {"id"})
    @PostMapping("/save")
    public BaseResult<String> add(${entity}VO vo){
        //校验参数, id不用传
        CheckUtils.checkExcludeFields(vo, "id").checkError();
        ${entity} entity = BeanUtils.parse(vo, ${entity}.class);
        return this.result(this.${entity?uncap_first}Service.create(entity), entity.getId());
    }

    @SaCheckPermission("${permission!}-edit")
    @Log("修改")
    @ApiOperation(value = "修改${tableDesc!}信息")
    @ApiOperationSupport(order = 36)
    @PostMapping("/save")
    public BaseResult<String> edit(${entity}VO vo){
        //校验参数
        CheckUtils.checkAllFields(vo).checkError();
        ${entity} entity = BeanUtils.parse(vo, ${entity}.class);
        return this.result(this.${entity?uncap_first}Service.update(entity), entity.getId());
    }

    @SaCheckPermission("${permission!}-delete")
    @Log("删除")
    @ApiOperation(value = "删除${tableDesc!}")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id",value = "${tableDesc!}ID", required = true)
    })
    @ApiOperationSupport(order = 40)
    @PostMapping("/removeById")
    public BaseResult<String> removeById(@RequestParam(required = false) String id){
        //校验ID不可为空
        CheckUtils.init().set(id, "ID").required().checkError();
        return this.result(this.${entity?uncap_first}Service.removeLogicById(id));
    }

}
</#if>
