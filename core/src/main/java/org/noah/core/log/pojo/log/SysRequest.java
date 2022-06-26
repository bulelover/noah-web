package org.noah.core.log.pojo.log;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author Noah
 * @since 2022-01-17
 */
@Getter
@Setter
@TableName("sys_request")
public class SysRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 请求地址
     */
    private String uri;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 访问IP
     */
    private String remoteAddr;

    /**
     * 浏览器名称
     */
    private String browserName;

    /**
     * 浏览器版本号
     */
    private String browserVersion;

    /**
     * 操作系统名称
     */
    private String osName;

    /**
     * 操作时间
     */
    private LocalDateTime createTime;

    /**
     * 操作人登录用户名
     */
    private String createLoginName;

    /**
     * 操作人真实姓名
     */
    private String createRealName;

    /**
     * 创建人用户ID
     */
    private String createUserId;

    /**
     * 1正常 0删除
     */
    private String flag;

}
