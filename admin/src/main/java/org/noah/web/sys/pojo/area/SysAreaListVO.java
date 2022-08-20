package org.noah.web.sys.pojo.area;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "SysAreaListVO对象", description = "行政区域表")
public class SysAreaListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键", position = 0)
    private String id;

    @ApiModelProperty(value = "区域编号", position = 5)
    private String code;

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

    @ApiModelProperty(value = "创建时间", position = 40)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人登录用户名", position = 45)
    private String createLoginName;

    @ApiModelProperty(value = "创建人真实姓名", position = 50)
    private String createRealName;

    @ApiModelProperty(value = "更新时间", position = 55)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人登录用户名", position = 60)
    private String updateLoginName;

    @ApiModelProperty(value = "更新人真实姓名", position = 65)
    private String updateRealName;

    @ApiModelProperty(value = "1正常 0禁用", position = 70)
    private String state;

    @ApiModelProperty(value = "删除标志 1正常 0删除", position = 75)
    private String flag;

    @ApiModelProperty(value = "创建人用户ID", position = 80)
    private String createUserId;

    @ApiModelProperty(value = "更新人用户ID", position = 85)
    private String updateUserId;

    @ApiModelProperty(value = "下级数量（仅在查询下级接口返回）", position = 90)
    private Integer childNum;

    @ApiModelProperty(value = "是否拥有下级（仅在查询下级接口返回）", position = 172)
    public Boolean getHasChildren(){
        return this.childNum != null && this.childNum > 0;
    }
    @ApiModelProperty(value = "是否最后一级（仅在查询下级接口返回）", position = 173)
    public Boolean getLeaf(){
        return !this.getHasChildren();
    }

}
