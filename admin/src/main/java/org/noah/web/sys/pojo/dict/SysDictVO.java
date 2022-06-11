package org.noah.web.sys.pojo.dict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 字典主表
 * </p>
 *
 * @author Noah
 * @since 2021-10-18
 */
@Getter
@Setter
@ApiModel(value = "SysDictVO")
public class SysDictVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("字典名称")
    private String name;

    @ApiModelProperty("字典编码")
    private String code;

    @ApiModelProperty("字典描述")
    private String remark;

    @ApiModelProperty("排序字段")
    private Integer orderBy;

}
