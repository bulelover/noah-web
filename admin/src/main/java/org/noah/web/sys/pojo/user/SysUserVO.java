package org.noah.web.sys.pojo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.noah.core.annotation.Check;

import java.io.Serializable;

@Data
@ApiModel(value="SysUserVO", description="用户保存接口入参")
public class SysUserVO implements Serializable {
    private static final long serialVersionUID = 7048466588862841854L;

    @Check(value = "用户ID",required = true)
    @ApiModelProperty(value = "用户ID", position = 10)
    private String id;

    @Check(value = "用户名",required = true, username = true)
    @ApiModelProperty(value = "登录用户名（新增必传，更新可不传）", position = 20)
    private String loginName;

    @Check(value = "真实姓名",required = true, maxlength = 20)
    @ApiModelProperty(value = "真实姓名", position = 40)
    private String realName;

    @Check(value = "性别",fields = {"1","2"})
    @ApiModelProperty(value = "性别", position = 50)
    private String sex;

    @Check(value = "联系电话",phone = true)
    @ApiModelProperty(value = "联系电话", position = 60)
    private String phone;

    @Check(value = "电子邮箱",mail = true)
    @ApiModelProperty(value = "电子邮箱", position = 70)
    private String mail;

    @Check(value = "身份证号",idCard = 1)
    @ApiModelProperty(value = "身份证号", position = 160)
    private String idNo;

    @Check(value = "联系电话2",phone = true)
    @ApiModelProperty(value = "联系电话2", position = 170)
    private String phone2;

    @Check(value = "职位岗位", maxlength = 100)
    @ApiModelProperty(value = "职位岗位", position = 180)
    private String position;

    @ApiModelProperty(value = "头像路径", position = 190)
    private String headImgPath;

    @Check(value = "状态",fields = {"1","0"})
    @ApiModelProperty(value = "状态标识 1正常 0锁定", position = 191)
    private String state;

}
