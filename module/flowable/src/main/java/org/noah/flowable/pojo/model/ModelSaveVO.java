package org.noah.flowable.pojo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "工作流模型保存")
public class ModelSaveVO {
    @ApiModelProperty("流程主键")
    private String id;
    @ApiModelProperty("BPMN XML文件")
    private String editor;
}
