package org.noah.web.sys.service.impl;

import org.noah.core.common.BaseServiceImpl;
import org.noah.web.sys.mapper.SysAreaMapper;
import org.noah.web.sys.pojo.area.SysArea;
import org.noah.web.sys.service.ISysAreaService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 行政区域表 服务实现类
 * </p>
 *
 * @author Noah
 * @since 2022-06-26
 */
@Service
public class SysAreaServiceImpl extends BaseServiceImpl<SysAreaMapper, SysArea> implements ISysAreaService {

    @Override
    protected String getOrderTableAlias(String column, Boolean isColumn) {
        if(isColumn){
            return "t."+column;
        }
        return null;
    }

    @Override
    public List<SysArea> selectChildren(String pid) {
        return baseMapper.selectChildren(pid);
    }
}
