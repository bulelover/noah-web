package org.noah.flowable.pojo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ApiModel(description = "工作流模型")
public class ModelVO {
    @ApiModelProperty("流程主键")
    private String id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("标识")
    private String key;
    @ApiModelProperty("类型")
    private String category;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date lastUpdateTime;
    @ApiModelProperty("版本号")
    private Integer version;
    @ApiModelProperty("部署ID")
    private String deploymentId;
    @ApiModelProperty("描述")
    private String metaInfo;
    @ApiModelProperty("租户ID")
    private String tenantId;
    @ApiModelProperty("BPMN文件")
    private String editor;
}
