package org.noah.web.sys.pojo.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noah.core.common.BasePage;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysMenuPage", description="菜单列表分页查询入参")
public class SysMenuPage extends BasePage<SysMenu> {

    private static final long serialVersionUID = 7083230669794497591L;

    @ApiModelProperty(value = "菜单名称", position = 20)
    private String name;

    @ApiModelProperty(value = "菜单编码", position = 30)
    private String code;

    @ApiModelProperty(value = "编码/名称", position = 30)
    private String search;

    @ApiModelProperty(value = "菜单URL", position = 40)
    private String url;

    @ApiModelProperty(value = "菜单类型 1菜单，2按钮", position = 50)
    private String type;

    @ApiModelProperty(value = "状态标识 1正常 0禁用", position = 60)
    private String state;

    @ApiModelProperty(value = "父菜单名称", position = 90)
    private String parentName;

    @ApiModelProperty(value = "平台类型 0 基础平台 1统计平台", position = 100)
    private String platformType;

    public String getCode() {
        return this.getLikeSql(code);
    }

    public String getName() {
        return this.getLikeSql(name);
    }

    public String getSearch() {
        return this.getLikeSql(search);
    }

    public String getUrl() {
        return this.getLikeSql(url);
    }

    public String getParentName() {
        return this.getLikeSql(parentName);
    }
}
