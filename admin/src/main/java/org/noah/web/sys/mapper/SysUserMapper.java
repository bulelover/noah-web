package org.noah.web.sys.mapper;

import org.apache.ibatis.annotations.Param;
import org.noah.core.common.BaseMapper;
import org.noah.web.sys.pojo.user.SysUser;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser getByLoginName(@Param("loginName") String loginName);

    List<SysUser> getUserByRoleId(@Param("roleId") String roleId);

    int deleteUserRolesByUserId(@Param("id") String id);

    int insertUserRole(@Param("id") String id, @Param("roleId")String roleId);

}
