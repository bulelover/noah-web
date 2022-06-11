package org.noah.core.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.noah.core.annotation.IgnoreSwaggerParameter;

import java.util.List;

@Setter
@Getter
public abstract class TreeNode {

    /**
     * 主键
     */
    @IgnoreSwaggerParameter
    private String id;
    /**
     * 标签
     */
    @IgnoreSwaggerParameter
    @TableField(exist = false)
    private String label;

    /**
     * 子节点
     */
    @IgnoreSwaggerParameter
    @TableField(exist = false)
    private List<TreeNode> children;

    /**
     * 子类需要实现此方法返回 父节点ID
     */
    @ApiModelProperty(hidden = true)
    public abstract String getNodeParentId();
    /**
     * 子类需要实现此方法返回 节点ID
     */
    @ApiModelProperty(hidden = true)
    public abstract String getNodeId();
    /**
     * 子类需要实现此方法返回 节点名称
     */
    @ApiModelProperty(hidden = true)
    public abstract String getNodeLabel();
}
