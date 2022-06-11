package org.noah.web.sys.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.noah.core.annotation.Log;
import org.noah.core.common.BaseController;
import org.noah.core.common.BaseResult;
import org.noah.core.common.PageResult;
import org.noah.core.utils.BaseUtils;
import org.noah.core.utils.BeanUtils;
import org.noah.core.utils.CheckUtils;
import org.noah.web.sys.pojo.menu.SysMenu;
import org.noah.web.sys.pojo.menu.SysMenuTreeVO;
import org.noah.web.sys.pojo.role.SysRole;
import org.noah.web.sys.pojo.role.SysRoleListVO;
import org.noah.web.sys.pojo.role.SysRolePage;
import org.noah.web.sys.pojo.role.SysRoleVO;
import org.noah.web.sys.service.ISysMenuService;
import org.noah.web.sys.service.ISysRoleService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
@Api(tags = "角色管理")
@ApiSort(104)
@RestController
@RequestMapping("/sys/role")
@Log("系统管理/角色管理")
public class SysRoleController extends BaseController {

    private final ISysRoleService sysRoleService;
    private final ISysMenuService sysMenuService;

    public SysRoleController(ISysRoleService sysRoleService, ISysMenuService sysMenuService) {
        this.sysRoleService = sysRoleService;
        this.sysMenuService = sysMenuService;
    }

    @SaCheckPermission("sys-role-view")
    @GetMapping("/getById")
    @ApiImplicitParam(name = "id",value = "角色ID")
    @ApiOperation(value = "根据ID查询角色")
    @ApiOperationSupport(order = 10)
    public BaseResult<SysRoleListVO> getById(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        SysRole entity = sysRoleService.getById(id);
        if(entity == null || "0".equals(entity.getFlag())){
            return this.failure("信息不存在");
        }
        return this.success(BeanUtils.parse(entity, SysRoleListVO.class));
    }

    @SaCheckPermission("sys-role")
    @PostMapping("/page")
    @ApiOperation(value = "角色分页列表查询")
    @ApiOperationSupport(order = 20)
    public BaseResult<PageResult<SysRoleListVO>> page(SysRolePage page){
        PageResult<SysRoleListVO> res = sysRoleService.selectPage(page,  SysRoleListVO.class);
        return this.success(res);
    }

    @SaCheckPermission("sys-role-add")
    @Log("新增")
    @ApiOperation(value = "新增角色信息")
    @ApiOperationSupport(order = 30, ignoreParameters = {"id"})
    @PostMapping("/save")
    public BaseResult<String> add(SysRoleVO vo){
        //校验参数, id不用传
        CheckUtils.checkExcludeFields(vo, "id").checkError();
        SysRole entity = BeanUtils.parse(vo, SysRole.class);
        return this.result(this.sysRoleService.create(entity), entity.getId());
    }

    @SaCheckPermission("sys-role-edit")
    @Log("修改")
    @ApiOperation(value = "修改角色信息")
    @ApiOperationSupport(order = 31)
    @PutMapping("/save")
    public BaseResult<String> edit(SysRoleVO vo){
        //校验参数 更新角色 不用传登录角色名
        CheckUtils.checkAllFields(vo).checkError();
        SysRole entity = BeanUtils.parse(vo, SysRole.class);
        return this.result(this.sysRoleService.update(entity), entity.getId());
    }

    @SaCheckPermission("sys-role-state")
    @Log("启用")
    @ApiOperation(value = "启用角色")
    @ApiImplicitParam(name = "id",value = "角色ID")
    @ApiOperationSupport(order = 34)
    @PutMapping("/enable")
    public BaseResult<String> enable(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        return this.result(this.sysRoleService.enable(id));
    }

    @SaCheckPermission("sys-role-state")
    @Log("禁用")
    @ApiOperation(value = "禁用角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "角色ID", required = true)
    })
    @ApiOperationSupport(order = 35)
    @PutMapping("/disable")
    public BaseResult<String> disable(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        return this.result(this.sysRoleService.disable(id));
    }

    @SaCheckPermission("sys-role-delete")
    @Log("删除")
    @ApiOperation(value = "删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "角色ID", required = true)
    })
    @ApiOperationSupport(order = 35)
    @DeleteMapping("/removeById")
    public BaseResult<String> removeById(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        return this.result(this.sysRoleService.removeLogicById(id));
    }

    @SaCheckPermission("sys-role-menu")
    @ApiOperation(value = "查询角色授权菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "角色ID", required = true)
    })
    @GetMapping("/getMenusByRoleId")
    public BaseResult<Map<String,Object>> getMenusByRoleId(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        List<SysMenuTreeVO> roleMenus = this.sysMenuService.getCacheMenuByRoleId(id);
        List<String> roleMenuIds = new ArrayList<>();
        for(SysMenuTreeVO treeVO: roleMenus){
            roleMenuIds.add(treeVO.getId());
        }
        List<SysMenu> allMenus = this.sysMenuService.getAllMenuList();
        Map<String,Object> res = new HashMap<>();
        res.put("selected", roleMenuIds);
        res.put("menus", BaseUtils.getSimpleTreeList(allMenus, ""));
        return this.success(res);
    }

    @SaCheckPermission("sys-role-menu")
    @Log("角色授权")
    @ApiOperation(value = "保存角色授权")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "角色ID", required = true),
            @ApiImplicitParam(name = "menuIds",value = "菜单Id，多个以逗号','隔开")
    })
    @PostMapping("/saveRoleMenu")
    public BaseResult<String> saveRoleMenu(@RequestParam(required = false) String id,
                                           @RequestParam(required = false) String menuIds){
        CheckUtils.init().set(id, "ID").required().checkError();
        this.sysRoleService.saveRoleMenu(id, menuIds);
        return this.success();
    }
}


