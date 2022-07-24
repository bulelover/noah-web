package org.noah.flowable.pojo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.flowable.engine.repository.Model;
import org.noah.core.common.BasePage;

/**
 * @author Noah
 * @since 2022-06-26
 */
@Getter
@Setter
@ApiModel(value = "FlwModelPage对象", description = "工作流模型分页查询入参")
public class FlwModelPage extends BasePage<Model> {

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("标识")
    private String key;
    @ApiModelProperty("类型")
    private String category;

}
