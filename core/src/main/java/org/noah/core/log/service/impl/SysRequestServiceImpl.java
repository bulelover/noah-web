package org.noah.core.log.service.impl;

import org.noah.core.common.BaseServiceImpl;
import org.noah.core.log.mapper.SysRequestMapper;
import org.noah.core.log.pojo.log.SysRequest;
import org.noah.core.log.service.ISysRequestService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author Noah
 * @since 2022-01-17
 */
@Service
public class SysRequestServiceImpl extends BaseServiceImpl<SysRequestMapper, SysRequest> implements ISysRequestService {

    @Override
    protected String getOrderTableAlias(String column, Boolean isColumn) {
        if(isColumn){
            return "t."+column;
        }
        return null;
    }

    @Async("taskExecutor")
    @Override
    public void createRequestLog(SysRequest log){
        log.setFlag("1");
        baseMapper.insert(log);
    }

}
