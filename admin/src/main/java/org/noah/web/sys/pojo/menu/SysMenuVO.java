package org.noah.web.sys.pojo.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noah.core.annotation.Check;

import java.io.Serializable;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysMenuVO", description="菜单保存接口入参")
public class SysMenuVO implements Serializable {

    private static final long serialVersionUID = 3021112792867259626L;

    @Check(value = "菜单ID",required = true)
    @ApiModelProperty(value = "主键", position = 10)
    private String id;

    @Check(value = "菜单名称",required = true, maxlength = 30)
    @ApiModelProperty(value = "菜单名称", position = 20)
    private String name;

    @Check(value = "菜单编码",required = true, code = true, maxlength = 50)
    @ApiModelProperty(value = "菜单编码", position = 30)
    private String code;

    @ApiModelProperty(value = "菜单URL", position = 40)
    private String url;

    @Check(value = "菜单类型",fields = {"1","2"})
    @ApiModelProperty(value = "菜单类型 1菜单，2按钮", position = 50)
    private String type;

    @Check(value = "状态",fields = {"0","1"})
    @ApiModelProperty(value = "状态标识 1正常 0禁用", position = 60)
    private String state;

    @ApiModelProperty(value = "图标", position = 70)
    private String icon;

    @ApiModelProperty(value = "菜单描述", position = 80)
    private String remark;

    @ApiModelProperty(value = "父菜单主键", position = 90)
    private String parentId;

    @Check(value = "平台类型",fields = {"0","1"})
    @ApiModelProperty(value = "平台类型 0 基础平台 1统计平台", position = 100)
    private String platformType;

    @Check(value = "排序", required = true, range = {0, 1000000000})
    @ApiModelProperty(value = "排序字段", position = 170)
    private Integer orderBy;

}
