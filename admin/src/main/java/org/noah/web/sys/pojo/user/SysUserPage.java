package org.noah.web.sys.pojo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noah.core.common.BasePage;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysUserPage", description="用户列表分页查询入参")
public class SysUserPage extends BasePage<SysUser> {

    private static final long serialVersionUID = -7639818591897454101L;

    @ApiModelProperty(value = "登录用户名|模糊", position = 20)
    private String loginName;

    @ApiModelProperty(value = "真实姓名|模糊", position = 40)
    private String realName;

    @ApiModelProperty(value = "用户名/姓名", position = 40)
    private String search;

    @ApiModelProperty(value = "性别", position = 50)
    private String sex;

    @ApiModelProperty(value = "联系电话", position = 60)
    private String phone;

    @ApiModelProperty(value = "状态标识 1正常 0锁定", position = 140)
    private String state;

    @ApiModelProperty(value = "身份证号", position = 160)
    private String idNo;

    @ApiModelProperty(value = "联系电话2", position = 170)
    private String phone2;

    @ApiModelProperty(value = "职位岗位|模糊", position = 180)
    private String position;

    //======= 模糊查询配置 这样配置可以适配不同的数据库（不使用数据库拼接） =======
    public String getLoginName() {
        return this.getLikeSql(loginName);
    }

    public String getRealName() {
        return this.getLikeSql(realName);
    }

    public String getPosition() {
        return this.getLikeSql(position);
    }
    public String getSearch() {
        return this.getLikeSql(search);
    }
}
