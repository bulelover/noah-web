package org.noah.web.sys.mapper;

import org.apache.ibatis.annotations.Param;
import org.noah.core.common.BaseMapper;
import org.noah.web.sys.pojo.role.SysRole;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> selectRolesByUserId(@Param("userId")String userId);

    List<SysRole> selectRolesByMenuId(@Param("menuId")String menuId);

    int deleteRoleMenusByRoleId(@Param("id") String id);

    int insertRoleMenu(@Param("id") String id, @Param("menuId")String menuId);

}
