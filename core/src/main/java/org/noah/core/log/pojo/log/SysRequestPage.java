package org.noah.core.log.pojo.log;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noah.core.common.BasePage;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysRequestPage", description="访问日志列表分页查询入参")
public class SysRequestPage extends BasePage<SysRequest> {

    private static final long serialVersionUID = 7083230669794497591L;

    @ApiModelProperty(value = "访问地址", position = 20)
    private String uri;

    @ApiModelProperty(value = "是否包含当前请求", position = 22)
    private String owner;

    @ApiModelProperty(value = "请求入参", position = 30)
    private String params;

    @ApiModelProperty(value = "请求方式", position = 40)
    private String method;

    @ApiModelProperty(value = "浏览器名称", position = 41)
    private String browserName;

    @ApiModelProperty(value = "浏览器版本", position = 42)
    private String browserVersion;

    @ApiModelProperty(value = "操作系统名称", position = 43)
    private String osName;

    @ApiModelProperty(value = "来源IP", position = 46)
    private String remoteAddr;

    @ApiModelProperty(value = "操作人", position = 47)
    private String createRealName;

    @ApiModelProperty(value = "操作人登录名", position = 48)
    private String createLoginName;

    @ApiModelProperty(value = "起始创建时间", position = 51)
    private LocalDateTime createTimeBegin;

    @ApiModelProperty(value = "结束创建时间", position = 52)
    private LocalDateTime createTimeEnd;

    public String getParams() {
        return this.getLikeSql(params);
    }
}
