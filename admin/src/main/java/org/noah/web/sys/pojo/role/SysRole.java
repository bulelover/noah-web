package org.noah.web.sys.pojo.role;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRole implements Serializable {

    /**
     * 主键 角色编码
     */
    private String id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述
     */
    private String remark;

    /**
     * 状态 1正常，0禁用
     */
    private String state;
    /**
     * 状态 本用户是否有此角色
     */
    @TableField(exist = false)
    private String has;

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
     * 创建人用户ID
     */
    @TableField("CREATE_USER_ID")
    private String createUserId;

    /**
     * 更新人用户ID
     */
    @TableField("UPDATE_USER_ID")
    private String updateUserId;

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
     * 删除标志 1正常 0删除
     */
    private String flag;

    /**
     * 排序字段
     */
    @TableField("ORDER_BY")
    private Integer orderBy;

}
