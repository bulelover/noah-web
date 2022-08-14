package org.noah.web.sys.pojo.org;

import com.alibaba.fastjson.annotation.JSONField;
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
 * 组织机构
 * </p>
 *
 * @author Noah
 * @since 2022-08-14
 */
@Getter
@Setter
@ApiModel(value = "SysOrgVO对象", description = "组织机构编辑入参")
public class SysOrgVO implements Serializable {

    private static final long serialVersionUID = 1L;

	@Check(value = "主键", required = true)
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

    @ApiModelProperty(value = "法定代表人", position = 70)
    private String legalRepresentative;

    @ApiModelProperty(value = "统一社会信用代码", position = 75)
    private String uscCode;

    @ApiModelProperty(value = "成立日期 date", position = 80)
    @JSONField(format = "yyyy-MM-dd")
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

    @ApiModelProperty(value = "备注", position = 135)
    private String remarks;

    @ApiModelProperty(value = "搜索码", position = 136)
    private String searchCode;

}
