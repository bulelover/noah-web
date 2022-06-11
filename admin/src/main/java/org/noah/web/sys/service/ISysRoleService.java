package org.noah.web.sys.service;

import org.noah.core.common.IBaseService;
import org.noah.web.sys.pojo.role.SysRole;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
public interface ISysRoleService extends IBaseService<SysRole> {

    /**
     * 保存角色授权
     * @param roleId 角色ID
     * @param menuIds 菜单ID
     */
    void saveRoleMenu(String roleId, String menuIds);

    boolean disable(String id);

    boolean enable(String id);

    List<SysRole> selectRolesByMenuId(String menuId);

    /**
     * 根据用户ID获取角色ID集合（缓存）
     * @param userId 用户ID
     * @return 角色ID集合
     */
    List<String> getCacheRoleByUserId(String userId);

    /**
     * 根据用户查询所有角色并标识该用户所拥有的角色
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectAllRolesByUserId(String userId);
}
