package org.noah.web.sys.pojo.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@ApiModel(value="SysMenuListVO", description="菜单列表分页查询出参")
public class SysMenuListVO implements Serializable {

    @ApiModelProperty(value = "主键", position = 10)
    private String id;

    @ApiModelProperty(value = "菜单名称", position = 20)
    private String name;

    @ApiModelProperty(value = "菜单编码", position = 30)
    private String code;

    @ApiModelProperty(value = "菜单URL", position = 40)
    private String url;

    @ApiModelProperty(value = "菜单类型 1菜单，2按钮", position = 50)
    private String type;

    @ApiModelProperty(value = "状态标识 1正常 0禁用", position = 60)
    private String state;

    @ApiModelProperty(value = "图标", position = 70)
    private String icon;

    @ApiModelProperty(value = "菜单描述", position = 80)
    private String remark;

    @ApiModelProperty(value = "父菜单主键", position = 90)
    private String parentId;

    @ApiModelProperty(value = "平台类型 0 基础平台 1统计平台", position = 100)
    private String platformType;

    @ApiModelProperty(value = "创建时间", position = 110)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人登录用户名", position = 120)
    private String createLoginName;

    @ApiModelProperty(value = "创建人真实姓名", position = 130)
    private String createRealName;

    @ApiModelProperty(value = "更新时间", position = 140)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人登录用户名", position = 150)
    private String updateLoginName;

    @ApiModelProperty(value = "更新人真实姓名", position = 160)
    private String updateRealName;

    @ApiModelProperty(value = "排序字段", position = 170)
    private Integer orderBy;

    @ApiModelProperty(value = "下级数量（仅在查询下级接口返回）", position = 172)
    private Integer childNum;

    @ApiModelProperty(value = "是否拥有下级（仅在查询下级接口返回）", position = 172)
    public Boolean getHasChildren(){
        return this.childNum != null && this.childNum > 0;
    }

}
