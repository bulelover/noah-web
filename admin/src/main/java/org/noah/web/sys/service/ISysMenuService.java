package org.noah.web.sys.service;

import org.noah.core.common.IBaseService;
import org.noah.web.sys.pojo.menu.SysMenu;
import org.noah.web.sys.pojo.menu.SysMenuTreeVO;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
public interface ISysMenuService extends IBaseService<SysMenu> {

    boolean disable(String id);

    boolean enable(String id);

    List<SysMenu> getAllMenuList();

    List<SysMenu> selectChildren(String pid);

    /**
     * 根据角色ID获取菜单集合（缓存）
     * @param roleId 角色ID
     * @return 菜单集合
     */
    List<SysMenuTreeVO> getCacheMenuByRoleId(String roleId);

    /**
     * 根据用户ID获取用户的菜单
     * @param userId 用户ID
     * @return 用户的菜单
     */
    List<SysMenuTreeVO> getUserMenus(String userId);

    /**
     * 根据用户ID获取用户的权限
     * @param userId 用户ID
     * @return 用户的权限
     */
    List<String> getUserPermissions(String userId);
}
