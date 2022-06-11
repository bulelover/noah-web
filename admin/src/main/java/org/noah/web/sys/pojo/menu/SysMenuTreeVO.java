package org.noah.web.sys.pojo.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noah.core.pojo.TreeNode;

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
@ApiModel(value="SysMenuTreeVO", description="获取用户菜单出参")
public class SysMenuTreeVO extends TreeNode implements Comparable<SysMenuTreeVO> {

    private static final long serialVersionUID = 3021112792867259626L;


    @ApiModelProperty(value = "主键", position = 10)
    private String id;

    @ApiModelProperty(value = "菜单名称", position = 20)
    private String name;

    @ApiModelProperty(value = "菜单编码", position = 30)
    private String code;

    @ApiModelProperty(value = "菜单URL", position = 40)
    private String url;

    @ApiModelProperty(value = "菜单类型 1菜单，2按钮", position = 50)
    private String type;

    @ApiModelProperty(value = "图标", position = 70)
    private String icon;

    @ApiModelProperty(value = "父菜单主键", position = 90)
    private String parentId;

    @ApiModelProperty(value = "排序码", position = 91)
    private Integer orderBy;


    @Override
    public int compareTo(SysMenuTreeVO o) {
        if(this.getOrderBy() > o.getOrderBy()){
            return 1;
        }
        return -1;
    }

    @Override
    public String getNodeParentId() {
        return this.parentId;
    }

    @Override
    public String getNodeId() {
        return this.id;
    }

    @Override
    public String getNodeLabel() {
        return this.name;
    }
}
