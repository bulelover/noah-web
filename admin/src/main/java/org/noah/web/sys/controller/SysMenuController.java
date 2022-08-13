package org.noah.web.sys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
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
import org.noah.core.satoken.TokenUtils;
import org.noah.core.utils.BaseUtils;
import org.noah.core.utils.BeanUtils;
import org.noah.core.utils.CheckUtils;
import org.noah.core.pojo.SimpleTreeNode;
import org.noah.core.pojo.TreeNode;
import org.noah.web.sys.pojo.menu.*;
import org.noah.web.sys.service.ISysDictService;
import org.noah.web.sys.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
@Api(tags = "菜单管理")
@ApiSort(106)
@Log("系统管理/菜单管理")
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {

    private final ISysMenuService sysMenuService;
    private final ISysDictService sysDictService;

    @Autowired
    public SysMenuController(ISysMenuService sysMenuService, ISysDictService sysDictService) {
        this.sysMenuService = sysMenuService;
        this.sysDictService = sysDictService;
    }

    @SaCheckLogin
    @GetMapping("/getLoginData")
    @ApiOperation(value = "查询菜单信息,权限信息，字典信息")
    @ApiOperationSupport(order = 1)
    public BaseResult<Map<String, Object>> getLoginData(){
        Map<String, Object> res = new HashMap<>();
        List<SysMenuTreeVO> tree = sysMenuService.getUserMenus(TokenUtils.getLoginUserId());
        List<TreeNode> menuList = BaseUtils.getTreeList(tree, "");
        List<String> permissions = sysMenuService.getUserPermissions(TokenUtils.getLoginUserId());
        Map<String, Object> dictList = sysDictService.getAllDictList();
        res.put("menuList", menuList);
        res.put("permissions", permissions);
        res.put("dictList", dictList);
        return this.success(res);
    }

    @SaCheckLogin
    @GetMapping("/getUserMenuList")
    @ApiOperation(value = "查询登录用户所拥有的菜单信息")
    @ApiOperationSupport(order = 1)
    public BaseResult<List<TreeNode>> getUserMenuList(){
        List<SysMenuTreeVO> tree = sysMenuService.getUserMenus(TokenUtils.getLoginUserId());
        return this.success(BaseUtils.getTreeList(tree, ""));
    }

    @SaCheckLogin
    @GetMapping("/getUserPermissions")
    @ApiOperation(value = "查询登录用户所有权限")
    @ApiOperationSupport(order = 2)
    public BaseResult<List<String>> getUserPermissions(){
        return this.success(sysMenuService.getUserPermissions(TokenUtils.getLoginUserId()));
    }

    @SaCheckPermission(value = {"sys-menu-add", "sys-menu-edit", "sys-menu-view"}, mode = SaMode.OR)
    @GetMapping("/getAllMenuList")
    @ApiOperation(value = "查询所有菜单信息")
    @ApiOperationSupport(order = 3)
    public BaseResult<List<SimpleTreeNode>> getAllMenuList(){
        List<SysMenu> allMenus = this.sysMenuService.getAllMenuList();
        return this.success(BaseUtils.getSimpleTreeList(allMenus, ""));
    }

    @SaCheckPermission("sys-menu-view")
    @GetMapping("/getById")
    @ApiImplicitParam(name = "id",value = "菜单ID")
    @ApiOperation(value = "根据ID查询菜单")
    @ApiOperationSupport(order = 10)
    public BaseResult<SysMenuListVO> getById(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        SysMenu entity = sysMenuService.getById(id);
        if(entity == null || "0".equals(entity.getFlag())){
            return this.failure("信息不存在");
        }
        return this.success(BeanUtils.parse(entity, SysMenuListVO.class));
    }

    @SaCheckPermission("sys-menu")
    @PostMapping("/page")
    @ApiOperation(value = "菜单分页列表查询")
    @ApiOperationSupport(order = 20)
    public BaseResult<PageResult<SysMenuListVO>> page(SysMenuPage page){
        PageResult<SysMenuListVO> res = sysMenuService.selectPage(page,  SysMenuListVO.class);
        return this.success(res);
    }

    @SaCheckPermission("sys-menu")
    @GetMapping("/children")
    @ApiOperation(value = "查询下级菜单列表")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(name = "pid",value = "上级菜单ID")
    public BaseResult<List<SysMenuListVO>> selectChildren(@RequestParam(required = false) String pid){
        List<SysMenu> res = sysMenuService.selectChildren(pid);
        return this.success(BeanUtils.parseList(res, SysMenuListVO.class));
    }

    @SaCheckPermission("sys-menu-add")
    @Log("新增")
    @ApiOperation(value = "新增菜单信息")
    @ApiOperationSupport(order = 30, ignoreParameters = {"id"})
    @PostMapping("/add")
    public BaseResult<String> add(SysMenuVO vo){
        //校验参数,id不用传
        CheckUtils.checkExcludeFields(vo, "id").checkError();
        SysMenu entity = BeanUtils.parse(vo, SysMenu.class);
        return this.result(this.sysMenuService.create(entity), entity.getId());
    }

    @SaCheckPermission("sys-menu-edit")
    @Log("修改")
    @ApiOperation(value = "修改菜单信息")
    @ApiOperationSupport(order = 31)
    @PostMapping("/edit")
    public BaseResult<String> edit(SysMenuVO vo){
        //校验参数
        CheckUtils.checkAllFields(vo).checkError();
        SysMenu entity = BeanUtils.parse(vo, SysMenu.class);
        return this.result(this.sysMenuService.update(entity), entity.getId());
    }

    @SaCheckPermission("sys-menu-state")
    @Log("启用")
    @ApiOperation(value = "启用菜单")
    @ApiImplicitParam(name = "id",value = "菜单ID")
    @ApiOperationSupport(order = 34)
    @PostMapping("/enable")
    public BaseResult<String> enable(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        return this.result(this.sysMenuService.enable(id));
    }

    @SaCheckPermission("sys-menu-state")
    @Log("禁用")
    @ApiOperation(value = "禁用菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "菜单ID", required = true)
    })
    @ApiOperationSupport(order = 35)
    @PostMapping("/disable")
    public BaseResult<String> disable(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        return this.result(this.sysMenuService.disable(id));
    }

    @SaCheckPermission("sys-menu-delete")
    @Log("删除")
    @ApiOperation(value = "删除菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "菜单ID", required = true)
    })
    @ApiOperationSupport(order = 35)
    @PostMapping("/removeById")
    public BaseResult<String> removeById(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        return this.result(this.sysMenuService.removeLogicById(id));
    }
}
