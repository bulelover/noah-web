package org.noah.web.sys.pojo.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value="SysRoleListVO", description="角色列表分页查询出参")
public class SysRoleListVO implements Serializable {

    @ApiModelProperty(value = "主键 角色编码", position = 10)
    private String id;

    @ApiModelProperty(value = "角色名称", position = 20)
    private String name;

    @ApiModelProperty(value = "角色描述", position = 30)
    private String remark;

    @ApiModelProperty(value = "状态 1正常，0禁用", position = 40)
    private String state;

    @ApiModelProperty(value = "排序字段", position = 42)
    private Integer orderBy;

    @ApiModelProperty(value = "创建时间", position = 50)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建人登录用户名", position = 60)
    private String createLoginName;

    @ApiModelProperty(value = "创建人真实姓名", position = 70)
    private String createRealName;

    @ApiModelProperty(value = "更新时间", position = 80)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新人登录用户名", position = 90)
    private String updateLoginName;

    @ApiModelProperty(value = "更新人真实姓名", position = 100)
    private String updateRealName;

}
