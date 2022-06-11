package org.noah.core.log.service;

import org.noah.core.common.IBaseService;
import org.noah.core.log.pojo.log.SysLog;
import org.springframework.scheduling.annotation.Async;

/**
 * <p>
 * 系统日志表 服务类
 * </p>
 *
 * @author Noah
 * @since 2021-08-03
 */
public interface ISysLogService extends IBaseService<SysLog> {

    /**
     * 异步创建日志
     * @param log 日志信息
     */
    @Async("taskExecutor")
    void createLog(SysLog log);
}
