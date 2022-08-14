package org.noah.web.sys.pojo.org;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 组织机构
 * </p>
 *
 * @author Noah
 * @since 2022-08-14
 */
@Getter
@Setter
@TableName("sys_org")
public class SysOrg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 组织机构代码
     */
    private String code;

    /**
     * 组织/企业/单位名称
     */
    private String name;

    /**
     * 组织类型（字典：org_type）select
     */
    private String type;

    /**
     * 状态1启用 0禁用
     */
    private String state;

    /**
     * 删除标志 1正常 0删除
     */
    private String flag;

    /**
     * 创建人用户ID
     */
    private String createUserId;

    /**
     * 更新人用户ID
     */
    private String updateUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人登录用户名
     */
    private String createLoginName;

    /**
     * 创建人真实姓名
     */
    private String createRealName;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人登录用户名
     */
    private String updateLoginName;

    /**
     * 更新人真实姓名
     */
    private String updateRealName;

    /**
     * 法定代表人
     */
    private String legalRepresentative;

    /**
     * 统一社会信用代码
     */
    private String uscCode;

    /**
     * 成立日期 date
     */
    private LocalDateTime establishmentDate;

    /**
     * 行政区划代码
     */
    private String areaCode;

    /**
     * 行政区划名称
     */
    private String areaName;

    /**
     * 地址
     */
    private String address;

    /**
     * 联系电话/手机号
     */
    private String phone;

    /**
     * 备用联系电话
     */
    private String phone2;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 行业性质（字典：industry）select
     */
    private String industry;

    /**
     * 简称
     */
    private String abbreviation;

    /**
     * 企业logo（图片ID）
     */
    private String logo;

    /**
     * 父级主键
     */
    private String parentId;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 搜索码
     */
    private String searchCode;

}
