package org.noah.web.sys.pojo.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noah.core.common.BasePage;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysRolePage", description="角色列表分页查询入参")
public class SysRolePage extends BasePage<SysRole> {

    private static final long serialVersionUID = -7639818591897454101L;
    @ApiModelProperty(value = "角色编码", position = 10)
    private String id;

    @ApiModelProperty(value = "角色名称", position = 20)
    private String name;

    @ApiModelProperty(value = "角色描述", position = 30)
    private String remark;

    @ApiModelProperty(value = "状态 1正常，0禁用", position = 40)
    private String state;

    //======= 模糊查询配置 这样配置可以适配不同的数据库（不使用数据库拼接） =======

    public String getId() {
        return this.getLikeSql(id);
    }

    public String getName() {
        return this.getLikeSql(name);
    }

    public String getRemark() {
        return this.getLikeSql(remark);
    }
}
