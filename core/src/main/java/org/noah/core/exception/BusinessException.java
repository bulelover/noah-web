package org.noah.core.exception;

/**
 * <p>
 *     自定义业务异常处理类
 * </p>
 * @author SouthXia
 * @since 2020.7.10
 */
public class BusinessException extends RuntimeException {

    private String message;

    public BusinessException(String message) {

        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
