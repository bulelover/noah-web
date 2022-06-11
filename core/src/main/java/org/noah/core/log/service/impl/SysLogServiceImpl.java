package org.noah.core.log.service.impl;

import org.noah.core.common.BaseServiceImpl;
import org.noah.core.log.mapper.SysLogMapper;
import org.noah.core.log.pojo.log.SysLog;
import org.noah.core.log.service.ISysLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author Noah
 * @since 2021-08-03
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Override
    protected String getOrderTableAlias(String column, Boolean isColumn) {
        if(isColumn){
            return "t."+column;
        }
        return null;
    }

    @Async("taskExecutor")
    @Override
    public void createLog(SysLog log){
        log.setFlag("1");
        if(log.getTransLevel() == null){
            log.setTransLevel(0);
        }
        baseMapper.insert(log);
    }
}
