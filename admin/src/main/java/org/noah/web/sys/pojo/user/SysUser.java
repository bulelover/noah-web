package org.noah.web.sys.pojo.user;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUser implements Serializable {


    /**
     * 主键
     */
    private String id;

    /**
     * 登录用户名
     */
    @TableField("LOGIN_NAME")
    private String loginName;

    /**
     * 登录密码
     */
    @TableField("LOGIN_PWD")
    private String loginPwd;

    /**
     * 真实姓名
     */
    @TableField("REAL_NAME")
    private String realName;

    /**
     * 性别
     */
    private String sex;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String mail;

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
     * 状态标识 1正常 0锁定
     */
    private String state;

    /**
     * 锁定原因
     */
    @TableField("LOCK_REASON")
    private String lockReason;

    /**
     * 身份证号
     */
    @TableField("ID_NO")
    private String idNo;

    /**
     * 联系电话2
     */
    private String phone2;

    /**
     * 职位岗位
     */
    private String position;

    /**
     * 头像路径
     */
    @TableField("HEAD_IMG_PATH")
    private String headImgPath;

    /**
     * 删除标志 1正常 0删除
     */
    private String flag;


    /**
     * 备注
     */
    @TableField("REMARKS")
    private String remarks;

    /**
     * 直属上级
     */
    @TableField("DIRECT_SUPERIOR")
    private String directSuperior;

    /**
     * 直属上级姓名
     */
    @TableField(value = "DIRECT_SUPERIOR_NAME",exist = false)
    private String directSuperiorName;

    /**
     * 昵称
     */
    @TableField("NICK_NAME")
    private String nickName;

    /**
     * 组织机构主键
     */
    @TableField("ORG_ID")
    private String orgId;

    @TableField(value = "ORG_NAME", exist = false)
    private String orgName;

    /**
     * 部门主键
     */
    @TableField("DEPARTMENT_ID")
    private String departmentId;

    @TableField(value = "DEPARTMENT_NAME", exist = false)
    private String departmentName;

}
