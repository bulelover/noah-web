package org.noah.web.sys.pojo.area;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 行政区域表
 * </p>
 *
 * @author Noah
 * @since 2022-06-26
 */
@Getter
@Setter
@TableName("sys_area")
public class SysArea implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 区域编号
     */
    private String code;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 父区域编号
     */
    private String pCode;

    /**
     * 区域等级
     */
    private Integer areaLevel;

    /**
     * 父级名称
     */
    private String pName;

    /**
     * 地区名称（全）
     */
    private String areaName;

    /**
     * 拼音码
     */
    private String pinyin;

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
     * 1正常 0禁用
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
     * 下级数量
     */
    @TableField(value = "CHILD_NUM", exist = false)
    private Integer childNum;
}
