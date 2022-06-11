package org.noah.core.pojo;

import lombok.Getter;
import lombok.Setter;
import org.noah.core.annotation.IgnoreSwaggerParameter;

import java.util.List;

@Setter
@Getter
public class SimpleTreeNode {

    /**
     * 主键
     */
    @IgnoreSwaggerParameter
    private String id;
    /**
     * 标签
     */
    @IgnoreSwaggerParameter
    private String label;

    /**
     * 子节点
     */
    @IgnoreSwaggerParameter
    private List<SimpleTreeNode> children;

}
