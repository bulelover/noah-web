package org.noah.web.sys.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
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
import org.noah.core.satoken.LoginUser;
import org.noah.core.satoken.TokenUtils;
import org.noah.core.utils.BeanUtils;
import org.noah.core.utils.CheckUtils;
import org.noah.web.sys.pojo.role.SysRole;
import org.noah.web.sys.pojo.user.SysUser;
import org.noah.web.sys.pojo.user.SysUserListVO;
import org.noah.web.sys.pojo.user.SysUserPage;
import org.noah.web.sys.pojo.user.SysUserVO;
import org.noah.web.sys.service.ISysRoleService;
import org.noah.web.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
@Api(tags = "用户管理")
@Log("系统管理/用户管理")
@ApiSort(102)
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {

    private final ISysUserService sysUserService;
    private final ISysRoleService sysRoleService;

    @Autowired
    public SysUserController(ISysUserService sysUserService, ISysRoleService sysRoleService) {
        this.sysUserService = sysUserService;
        this.sysRoleService = sysRoleService;
    }

    @SaCheckLogin
    @GetMapping("/info")
    @ApiOperation(value = "查询登录用户个人信息")
    @ApiOperationSupport(order = 1)
    public BaseResult<LoginUser> info(){
        return this.success(TokenUtils.getLoginUser());
    }

    @SaCheckPermission("sys-user-view")
    @GetMapping("/getById")
    @ApiImplicitParam(name = "id",value = "用户ID")
    @ApiOperation(value = "根据ID查询用户")
    @ApiOperationSupport(order = 10)
    public BaseResult<SysUserListVO> getById(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        SysUser entity = sysUserService.getById(id);
        if(entity == null || "0".equals(entity.getFlag())){
            return this.failure("信息不存在");
        }
        return this.success(BeanUtils.parse(entity, SysUserListVO.class));
    }

    @SaCheckPermission("sys-user")
    @PostMapping("/page")
    @ApiOperation(value = "用户分页列表查询")
    @ApiOperationSupport(order = 20)
    public BaseResult<PageResult<SysUserListVO>> page(SysUserPage page){
        PageResult<SysUserListVO> res = sysUserService.selectPage(page,  SysUserListVO.class);
        return this.success(res);
    }

    @SaCheckLogin
    @Log("修改个人信息")
    @ApiOperation(value = "修改个人信息")
    @ApiOperationSupport(order = 31, ignoreParameters = {"loginName","state"})
    @PutMapping("/savePersonal")
    public BaseResult<String> savePersonal(SysUserVO vo){
        //校验参数 修改个人信息 不用传登录用户名和状态
        CheckUtils.checkExcludeFields(vo,
                BeanUtils.getPropertyName(SysUser::getState),
                BeanUtils.getPropertyName(SysUser::getLoginName)).checkError();
        SysUser entity = BeanUtils.parse(vo, SysUser.class);
        return this.result(this.sysUserService.update(entity), entity.getId());
    }

    @SaCheckLogin
    @Log("修改头像")
    @ApiOperation(value = "修改头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户ID", required = true),
            @ApiImplicitParam(name = "imgId",value = "头像ID")
    })
    @PutMapping("/saveImg")
    public BaseResult<String> saveImg(@RequestParam("id") String id, @RequestParam("imgId")  String imgId){
        this.sysUserService.updateImg(id, imgId);
        return this.result(true);
    }

    @SaCheckPermission("sys-user-add")
    @Log("新增")
    @ApiOperation(value = "新增用户信息")
    @ApiOperationSupport(order = 30, ignoreParameters = {"id"})
    @PostMapping("/save")
    public BaseResult<String> add(SysUserVO vo){
        //校验参数, id不用传
        CheckUtils.checkExcludeFields(vo, "id").checkError();
        SysUser entity = BeanUtils.parse(vo, SysUser.class);
        return this.result(this.sysUserService.create(entity), entity.getId());
    }

    @SaCheckPermission("sys-user-edit")
    @Log("修改")
    @ApiOperation(value = "修改用户信息")
    @ApiOperationSupport(order = 31, ignoreParameters = {"loginName"})
    @PutMapping("/save")
    public BaseResult<String> edit(SysUserVO vo){
        //校验参数 更新用户 不用传登录用户名
        CheckUtils.checkExcludeFields(vo, BeanUtils.getPropertyName(SysUser::getLoginName)).checkError();
        SysUser entity = BeanUtils.parse(vo, SysUser.class);
        return this.result(this.sysUserService.update(entity), entity.getId());
    }

    @SaCheckPermission("sys-user-state")
    @Log("启用")
    @ApiOperation(value = "启用用户")
    @ApiImplicitParam(name = "id",value = "用户ID")
    @ApiOperationSupport(order = 34)
    @PutMapping("/enable")
    public BaseResult<String> enable(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        return this.result(this.sysUserService.enable(id));
    }

    @SaCheckPermission("sys-user-state")
    @Log("禁用")
    @ApiOperation(value = "禁用用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户ID", required = true),
            @ApiImplicitParam(name = "reason",value = "原因")
    })
    @ApiOperationSupport(order = 35)
    @PutMapping("/disable")
    public BaseResult<String> disable(@RequestParam(required = false) String id,
                                      @RequestParam(required = false) String reason){
        CheckUtils.init().set(id, "ID").required().checkError();
        boolean r = this.sysUserService.disable(id, reason);
        if(r){
            StpUtil.logoutByLoginId(id);
        }
        return this.result(r);
    }

    @SaCheckPermission("sys-user-delete")
    @Log("删除")
    @ApiOperation(value = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户ID", required = true)
    })
    @ApiOperationSupport(order = 36)
    @DeleteMapping("/removeById")
    public BaseResult<String> removeById(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        boolean r = this.sysUserService.removeLogicById(id);
        if(r){
            StpUtil.logoutByLoginId(id);
        }
        return this.result(r);
    }

    @SaCheckPermission("sys-user-role")
    @GetMapping("/getUserRoles")
    @ApiImplicitParam(name = "id",value = "用户ID")
    @ApiOperation(value = "查询所有角色 并标识用户所拥有的角色")
    @ApiOperationSupport(order = 10)
    public BaseResult<List<SysRole>> selectAllRolesById(@RequestParam(required = false) String id){
        List<SysRole> list = sysRoleService.selectAllRolesByUserId(id);
        return this.success(list);
    }

    @SaCheckPermission("sys-user-role")
    @Log("分配角色")
    @ApiOperation(value = "分配角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户ID", required = true),
            @ApiImplicitParam(name = "roleIds",value = "角色Id，多个以逗号','隔开")
    })
    @PostMapping("/saveUserRole")
    public BaseResult<String> saveUserRole(@RequestParam(required = false) String id,
                                           @RequestParam(required = false) String roleIds){
        CheckUtils.init().set(id, "ID").required().checkError();
        this.sysUserService.saveUserRole(id, roleIds);
        return this.success();
    }
}
