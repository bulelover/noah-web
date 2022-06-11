package org.noah.web.sys.pojo.menu;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noah.core.pojo.TreeNode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class SysMenu extends TreeNode implements Serializable {


    /**
     * 主键
     */
    private String id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单编码
     */
    private String code;

    /**
     * 菜单URL
     */
    private String url;

    /**
     * 菜单类型 1菜单，2按钮
     */
    private String type;

    /**
     * 状态标识 1正常 0禁用
     */
    private String state;

    /**
     * 图标
     */
    private String icon;

    /**
     * 菜单描述
     */
    private String remark;

    /**
     * 父菜单主键
     */
    @TableField("PARENT_ID")
    private String parentId;

    /**
     * 0 基础平台 1统计平台（暂定）
     */
    @TableField("PLATFORM_TYPE")
    private String platformType;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 创建人登录用户名
     */
    @TableField("CREATE_LOGIN_NAME")
    private String createLoginName;

    /**
     * 创建人真实姓名
     */
    @TableField("CREATE_REAL_NAME")
    private String createRealName;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 更新人登录用户名
     */
    @TableField("UPDATE_LOGIN_NAME")
    private String updateLoginName;

    /**
     * 更新人真实姓名
     */
    @TableField("UPDATE_REAL_NAME")
    private String updateRealName;

    /**
     * 排序字段
     */
    @TableField("ORDER_BY")
    private Integer orderBy;

    /**
     * 删除标志 1正常 0删除
     */
    private String flag;

    /**
     * 下级数量
     */
    @TableField(value = "CHILD_NUM", exist = false)
    private Integer childNum;


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
