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

}
