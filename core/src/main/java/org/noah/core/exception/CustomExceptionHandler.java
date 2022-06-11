package org.noah.core.exception;

import org.noah.core.common.BaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 *     自定义异常处理类
 * </p>
 * @author SouthXia
 * @since 2020.7.10
 */
@RestControllerAdvice
public class CustomExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    /**
     * 捕获全局异常，处理所有不可知的异常
     * @param e 异常对象
     * @return 统一结果集
     */
    @ExceptionHandler(Exception.class)
    public BaseResult<String> handleGlobalException(Exception e){
        log.error("出现全局异常错误:{}", e.getMessage());
        if(log.isDebugEnabled()){
            e.printStackTrace();
        }
        return BaseResult.failure(e.getMessage() == null?e.toString():e.getMessage());
    }

    /**
     * 捕获自定义异常
     * @param e 异常对象
     * @return 统一结果集
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResult<String> handleMyException(BusinessException e){
        log.error("出现自定义异常错误:{}", e.getMessage());
        return BaseResult.failure(e.getMessage() == null?e.toString():e.getMessage());
    }
}
