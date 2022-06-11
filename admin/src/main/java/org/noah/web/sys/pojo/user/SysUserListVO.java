package org.noah.web.sys.pojo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value="SysUserListVO", description="用户列表分页查询出参")
public class SysUserListVO implements Serializable {

    private static final long serialVersionUID = 5514437940085918184L;

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

    @ApiModelProperty(value = "状态标识 1正常 0锁定", position = 140)
    private String state;

    @ApiModelProperty(value = "锁定原因", position = 150)
    private String lockReason;

    @ApiModelProperty(value = "身份证号", position = 160)
    private String idNo;

    @ApiModelProperty(value = "联系电话2", position = 170)
    private String phone2;

    @ApiModelProperty(value = "职位岗位", position = 180)
    private String position;

    @ApiModelProperty(value = "头像路径", position = 190)
    private String headImgPath;

    @ApiModelProperty(value = "创建时间", position = 192)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人真实姓名", position = 193)
    private String createRealName;

    @ApiModelProperty(value = "更新时间", position = 196)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人真实姓名", position = 197)
    private String updateRealName;


}
