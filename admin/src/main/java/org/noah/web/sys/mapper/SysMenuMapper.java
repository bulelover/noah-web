package org.noah.web.sys.mapper;

import org.apache.ibatis.annotations.Param;
import org.noah.core.common.BaseMapper;
import org.noah.web.sys.pojo.menu.SysMenu;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> selectMenusByRoleId(@Param("roleId")String roleId);

    List<SysMenu> selectChildren(@Param("pid") String pid);

    List<SysMenu> getAllMenuList();
}
