package org.noah.web.sys.pojo.org;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 组织机构
 * </p>
 *
 * @author Noah
 * @since 2022-08-14
 */
@Getter
@Setter
@ApiModel(value = "SysOrgListVO对象", description = "组织机构")
public class SysOrgListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键", position = 0)
    private String id;

    @ApiModelProperty(value = "组织机构代码", position = 5)
    private String code;

    @ApiModelProperty(value = "组织/企业/单位名称", position = 10)
    private String name;

    @ApiModelProperty(value = "组织类型（字典：org_type）select", position = 15)
    private String type;

    @ApiModelProperty(value = "状态1启用 0禁用", position = 20)
    private String state;

    @ApiModelProperty(value = "删除标志 1正常 0删除", position = 25)
    private String flag;

    @ApiModelProperty(value = "创建人用户ID", position = 30)
    private String createUserId;

    @ApiModelProperty(value = "更新人用户ID", position = 35)
    private String updateUserId;

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

    @ApiModelProperty(value = "法定代表人", position = 70)
    private String legalRepresentative;

    @ApiModelProperty(value = "统一社会信用代码", position = 75)
    private String uscCode;

    @ApiModelProperty(value = "成立日期 date", position = 80)
    private LocalDateTime establishmentDate;

    @ApiModelProperty(value = "行政区划代码", position = 85)
    private String areaCode;

    @ApiModelProperty(value = "行政区划名称", position = 90)
    private String areaName;

    @ApiModelProperty(value = "地址", position = 95)
    private String address;

    @ApiModelProperty(value = "联系电话/手机号", position = 100)
    private String phone;

    @ApiModelProperty(value = "备用联系电话", position = 105)
    private String phone2;

    @ApiModelProperty(value = "邮箱", position = 110)
    private String mail;

    @ApiModelProperty(value = "行业性质（字典：industry）select", position = 115)
    private String industry;

    @ApiModelProperty(value = "简称", position = 120)
    private String abbreviation;

    @ApiModelProperty(value = "企业logo（图片ID）", position = 125)
    private String logo;

    @ApiModelProperty(value = "父级主键", position = 130)
    private String parentId;

    @ApiModelProperty(value = "父级机构名称", position = 130)
    private String parentName;

    @ApiModelProperty(value = "备注", position = 135)
    private String remarks;

    @ApiModelProperty(value = "搜索码", position = 136)
    private String searchCode;

    @ApiModelProperty(value = "层级ID", position = 136)
    private String treeIds;

    @ApiModelProperty(value = "下级数量（仅在查询下级接口返回）", position = 170)
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
