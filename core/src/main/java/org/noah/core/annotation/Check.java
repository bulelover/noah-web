package org.noah.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段校验注解实现方式
 * @author SouthXia
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface Check {

    /**
     * 这里配置定义字符串单个中文字符所占字节的长度
     * 依据数据库存储的字符集编码来配置
     * 例如UTF8 配置3
     * GBK，GB2312等字符集配置 2
     */
    int CHINESE_LEN= 3;

    /**
     * 身份证号 1 标准验证
     */
    int IC_STANDARD = 1;

    /**
     * 身份证号 2简单验证
     */
    int IC_SIMPLE = 2;

    String value() default ""; //名称
    //为空验证
    boolean required() default false;
    //范围验证
    double[] range() default {};
    //整数验证
    boolean integer() default false;
    //正整数
    int positiveInteger() default 0;
    //验证字符串最大长度
    int maxlength() default 0;
    //验证固定值域范围
    String[] fields() default {};
    //精度验证(参考Oracle方式 如NUMBER(1) {1}   如NUMBER(4,2) {4,2})
    int[] precision() default {};
    //身份证号 1标准验证 2简单验证
    int idCard() default 0;
    //电话号码
    boolean phone() default false;
    boolean mail() default false;
    boolean username() default false;
    boolean password() default false;
    //只能输入字母，数字，下划线，减号
    boolean code() default false;
    //验证小数位长度
    int maxDecimalPlace() default -1;
    //验证字符串 日期格式 yyyy-MM-dd
    String date() default "";
    //验证年份合法性
    boolean year() default false;
    //验证月份合法性
    boolean month() default false;
    //验证最小时间范围
    boolean minTime() default false;
    //验证最大时间范围
    boolean maxTime() default false;
    //验证字典编码合法性 传入值为相应字典编码
    String dictValue() default "";
    //验证字典名称合法性 传入值为相应字典编码
    String dictKey() default "";
    //自定义pattern正则验证
    String pattern() default "";
    //自定义pattern的错误提示
    String msg() default "不合法";
}
