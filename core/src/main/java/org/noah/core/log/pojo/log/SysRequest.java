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
@ApiModel(value = "SysRequest对象", description = "系统日志表")
public class SysRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("请求地址")
    private String uri;

    @ApiModelProperty("请求参数")
    private String params;

    @ApiModelProperty("请求方式")
    private String method;

    @ApiModelProperty("访问IP")
    private String remoteAddr;

    @ApiModelProperty("浏览器名称")
    private String browserName;

    @ApiModelProperty("浏览器版本号")
    private String browserVersion;

    @ApiModelProperty("操作系统名称")
    private String osName;

    @ApiModelProperty("操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty("操作人登录用户名")
    private String createLoginName;

    @ApiModelProperty("操作人真实姓名")
    private String createRealName;

    @ApiModelProperty("1正常 0删除")
    private String flag;

}
