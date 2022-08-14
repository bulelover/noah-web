package org.noah.core.satoken;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1393293293273353246L;

    @ApiModelProperty(value = "用户ID", position = 10)
    private String id;

    @ApiModelProperty(value = "登录用户名", position = 20)
    private String loginName;

    @ApiModelProperty(value = "真实姓名", position = 40)
    private String realName;

    @ApiModelProperty(value = "性别", position = 50)
    private String sex;

    @ApiModelProperty(value = "联系电话", position = 60)
    private String phone;

    @ApiModelProperty(value = "电子邮箱", position = 70)
    private String mail;

    @ApiModelProperty(value = "身份证号", position = 160)
    private String idNo;

    @ApiModelProperty(value = "联系电话2", position = 170)
    private String phone2;

    @ApiModelProperty(value = "职位岗位", position = 180)
    private String position;

    @ApiModelProperty(value = "头像路径", position = 190)
    private String headImgPath;

    @ApiModelProperty(value = "备注", position = 200)
    private String remarks;

    @ApiModelProperty(value = "直属上级", position = 202)
    private String directSuperior;

    @ApiModelProperty(value = "直属上级姓名", position = 203)
    private String directSuperiorName;

    @ApiModelProperty(value = "昵称", position = 210)
    private String nickName;

    @ApiModelProperty(value = "组织机构主键", position = 220)
    private String orgId;

    @ApiModelProperty(value = "组织机构名称", position = 222)
    private String orgName;

    @ApiModelProperty(value = "部门主键", position = 224)
    private String departmentId;

    @ApiModelProperty(value = "部门名称", position = 226)
    private String departmentName;
}
