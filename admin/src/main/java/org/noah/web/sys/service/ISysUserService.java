package org.noah.web.sys.service;

import org.noah.core.common.IBaseService;
import org.noah.web.sys.pojo.user.SysUser;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
public interface ISysUserService extends IBaseService<SysUser> {

    /**
     * 根据登录名查询用户信息
     * @param loginName 登录名
     * @return 用户信息
     */
    SysUser getByLoginName(String loginName);

    /**
     * 禁用用户信息
     * @param id 用户ID
     * @return 是否成功
     */
    boolean disable(String id, String lockReason);

    /**
     * 启用用户信息
     * @param id 用户ID
     * @return 是否成功
     */
    boolean enable(String id);

    void updateImg(String id, String imgId);

    boolean isAdmin(String userId);

    /**
     * 保存用户分配的角色
     * @param id 用户ID
     * @param roleIds 角色Id，多个以逗号','隔开
     */
    void saveUserRole(String id, String roleIds);
}
