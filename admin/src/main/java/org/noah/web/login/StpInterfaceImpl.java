package org.noah.web.login;

import cn.dev33.satoken.stp.StpInterface;
import org.noah.web.sys.service.ISysMenuService;
import org.noah.web.sys.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {

    private final ISysMenuService sysMenuService;

    private final ISysRoleService roleService;

    @Autowired
    public StpInterfaceImpl(ISysMenuService sysMenuService, ISysRoleService roleService) {
        this.sysMenuService = sysMenuService;
        this.roleService = roleService;
    }

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return sysMenuService.getUserPermissions((String) loginId);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return roleService.getCacheRoleByUserId((String) loginId);
    }
}
