package org.noah.core.log.service;

import org.noah.core.common.IBaseService;
import org.noah.core.log.pojo.log.SysRequest;
import org.springframework.scheduling.annotation.Async;

/**
 * <p>
 * 系统日志表 服务类
 * </p>
 *
 * @author Noah
 * @since 2022-01-17
 */
public interface ISysRequestService extends IBaseService<SysRequest> {

    /**
     * 异步记录请求日志
     * @param log 日志信息
     */
    @Async("taskExecutor")
    void createRequestLog(SysRequest log);
}
