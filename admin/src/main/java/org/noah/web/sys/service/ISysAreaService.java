package org.noah.web.sys.service;

import org.noah.core.common.IBaseService;
import org.noah.web.sys.pojo.area.SysArea;

import java.util.List;

/**
 * <p>
 * 行政区域表 服务类
 * </p>
 *
 * @author Noah
 * @since 2022-06-26
 */
public interface ISysAreaService extends IBaseService<SysArea> {

    /**
     * 根据pid查询下级的区划
     * @param pid 上级id
     * @return List<SysMenu>
     */
    List<SysArea> selectChildren(String pid);
}
