package org.noah.web.sys.mapper;

import org.apache.ibatis.annotations.Param;
import org.noah.web.sys.pojo.dict.SysDictItem;
import org.noah.core.common.BaseMapper;

import java.util.List;

/**
 * <p>
 * 字典明细表 Mapper 接口
 * </p>
 *
 * @author Noah
 * @since 2021-10-18
 */
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {

    List<SysDictItem> selectByDictId(@Param("dictId") String dictId);
}
