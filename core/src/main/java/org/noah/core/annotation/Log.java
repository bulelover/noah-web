package org.noah.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志记录注解类
 * @author SouthXia
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    //日志交易摘要
    String value() default "";
    //交易类型 0查询类  1业务类 2登录类
    int type() default 1;
    //交易厂商（非系统内部交易）
    String firm() default "";

    int TYPE_QUERY = 0;
}
