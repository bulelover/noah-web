package org.noah.web.sys.pojo.area;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.noah.core.annotation.Check;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 行政区域表
 * </p>
 *
 * @author Noah
 * @since 2022-06-26
 */
@Getter
@Setter
@ApiModel(value = "SysAreaVO对象", description = "行政区域表编辑入参")
public class SysAreaVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Check(value = "主键", required = true)
    @ApiModelProperty(value = "主键", position = 0)
    private String id;

    @Check(value = "区域编号", required = true)
    @ApiModelProperty(value = "区域编号", position = 5)
    private String code;

    @Check(value = "区域名称", required = true)
    @ApiModelProperty(value = "区域名称", position = 10)
    private String name;

    @ApiModelProperty(value = "父区域编号", position = 15)
    private String pCode;

    @ApiModelProperty(value = "区域等级", position = 20)
    private Integer areaLevel;

    @ApiModelProperty(value = "父级名称", position = 25)
    private String pName;

    @ApiModelProperty(value = "地区名称（全）", position = 30)
    private String areaName;

    @ApiModelProperty(value = "拼音码", position = 35)
    private String pinyin;

    @Check(value = "状态", required = true)
    @ApiModelProperty(value = "1正常 0禁用", position = 70)
    private String state;


}
