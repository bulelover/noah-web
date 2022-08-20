package org.noah.web.sys.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.noah.core.annotation.Log;
import org.noah.core.common.BaseController;
import org.noah.core.common.BaseResult;
import org.noah.core.common.PageResult;
import org.noah.core.utils.BeanUtils;
import org.noah.core.utils.CheckUtils;
import org.noah.web.sys.pojo.org.SysOrg;
import org.noah.web.sys.pojo.org.SysOrgListVO;
import org.noah.web.sys.pojo.org.SysOrgPage;
import org.noah.web.sys.pojo.org.SysOrgVO;
import org.noah.web.sys.service.ISysOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 组织机构 前端控制器
 * </p>
 *
 * @author Noah
 * @since 2022-08-14
 */
@Api(tags = "组织管理")
@ApiSort(91)
@Log("系统管理/组织管理")
@RestController
@RequestMapping("/sys/org")
public class SysOrgController extends BaseController {

    private final ISysOrgService sysOrgService;

    @Autowired
    public SysOrgController(ISysOrgService sysOrgService) {
        this.sysOrgService = sysOrgService;
    }

    @SaCheckPermission("sys-org-view")
    @GetMapping("/getById")
    @ApiImplicitParam(name = "id", value = "组织ID")
    @ApiOperation(value = "根据ID查询组织")
    @ApiOperationSupport(order = 10)
    public BaseResult<SysOrgListVO> getById(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        SysOrg entity = sysOrgService.getById(id);
        if(entity == null || "0".equals(entity.getFlag())){
            return this.failure("信息不存在");
        }
        SysOrgListVO vo = BeanUtils.parse(entity, SysOrgListVO.class);
        if(StringUtils.isNotBlank(entity.getParentId())){
            SysOrg po = sysOrgService.getById(id);
            vo.setParentName(po == null?"":po.getName());
        }
        return this.success(vo);
    }

    @SaCheckPermission("sys-org")
    @PostMapping("/page")
    @ApiOperation(value = "组织分页列表查询")
    @ApiOperationSupport(order = 20)
    public BaseResult<PageResult<SysOrgListVO>> page(SysOrgPage page){
        PageResult<SysOrgListVO> res = sysOrgService.selectPage(page,  SysOrgListVO.class);
        return this.success(res);
    }
    @SaCheckPermission
    @GetMapping("/children")
    @ApiOperation(value = "查询下级组织列表")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(name = "pid",value = "上级组织ID")
    public BaseResult<List<SysOrgListVO>> selectChildren(@RequestParam(required = false) String pid){
        List<SysOrg> res = sysOrgService.selectChildren(pid);
        return this.success(BeanUtils.parseList(res, SysOrgListVO.class));
    }

    @SaCheckPermission("sys-org-add")
    @Log("新增")
    @ApiOperation(value = "新增组织信息")
    @ApiOperationSupport(order = 30, ignoreParameters = {"id"})
    @PostMapping("/add")
    public BaseResult<String> add(SysOrgVO vo){
        //校验参数, id不用传
        CheckUtils.checkExcludeFields(vo, "id").checkError();
        SysOrg entity = BeanUtils.parse(vo, SysOrg.class);
        return this.result(this.sysOrgService.create(entity), entity.getId());
    }

    @SaCheckPermission("sys-org-edit")
    @Log("修改")
    @ApiOperation(value = "修改组织信息")
    @ApiOperationSupport(order = 36)
    @PostMapping("/edit")
    public BaseResult<String> edit(SysOrgVO vo){
        //校验参数
        CheckUtils.checkAllFields(vo).checkError();
        SysOrg entity = BeanUtils.parse(vo, SysOrg.class);
        return this.result(this.sysOrgService.update(entity), entity.getId());
    }

    @SaCheckPermission("sys-org-delete")
    @Log("删除")
    @ApiOperation(value = "删除组织")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id",value = "组织ID", required = true)
    })
    @ApiOperationSupport(order = 40)
    @PostMapping("/removeById")
    public BaseResult<String> removeById(@RequestParam(required = false) String id){
        //校验ID不可为空
        CheckUtils.init().set(id, "ID").required().checkError();
        return this.result(this.sysOrgService.removeLogicById(id));
    }

}
