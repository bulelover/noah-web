package org.noah.web.sys.mapper;

import org.apache.ibatis.annotations.Param;
import org.noah.core.common.BaseMapper;
import org.noah.web.sys.pojo.area.SysArea;

import java.util.List;

/**
 * <p>
 * 行政区域表 Mapper 接口
 * </p>
 *
 * @author Noah
 * @since 2022-06-26
 */
public interface SysAreaMapper extends BaseMapper<SysArea> {

    List<SysArea> selectChildren(@Param("pid") String pid);
}
