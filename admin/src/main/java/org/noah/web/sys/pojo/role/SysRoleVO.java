package org.noah.web.sys.pojo.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.noah.core.annotation.Check;

import java.io.Serializable;

@Data
@ApiModel(value="SysRoleVO", description="角色保存接口入参")
public class SysRoleVO implements Serializable {

    @ApiModelProperty(value = "主键 角色编码", position = 10)
    @Check(required = true)
    private String id;

    @ApiModelProperty(value = "角色名称", position = 20)
    @Check(required = true)
    private String name;

    @ApiModelProperty(value = "角色描述", position = 30)
    private String remark;

    @ApiModelProperty(value = "状态 1正常，0禁用", position = 40)
    private String state;

    @ApiModelProperty(value = "排序字段", position = 42)
    private Integer orderBy;

}
