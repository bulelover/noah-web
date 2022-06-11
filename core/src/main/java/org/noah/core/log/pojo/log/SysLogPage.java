package org.noah.core.log.pojo.log;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noah.core.common.BasePage;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysLogPage", description="日志列表分页查询入参")
public class SysLogPage extends BasePage<SysLog> {

    private static final long serialVersionUID = 7083230669794497591L;

    @ApiModelProperty(value = "交易摘要", position = 20)
    private String transName;

    @ApiModelProperty(value = "交易入参", position = 30)
    private String transIn;

    @ApiModelProperty(value = "交易类型", position = 40)
    private String transType;

    @ApiModelProperty(value = "来源IP", position = 46)
    private String transIp;

    @ApiModelProperty(value = "操作人", position = 47)
    private String createRealName;

    @ApiModelProperty(value = "操作人登录名", position = 48)
    private String createLoginName;

    @ApiModelProperty(value = "起始创建时间", position = 51)
    private LocalDateTime createTimeBegin;

    @ApiModelProperty(value = "结束创建时间", position = 52)
    private LocalDateTime createTimeEnd;

    public String getTransName() {
        return this.getLikeSql(transName);
    }
    public String getTransIn() {
        return this.getLikeSql(transIn);
    }
}
