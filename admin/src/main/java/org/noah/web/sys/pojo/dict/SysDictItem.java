package org.noah.web.sys.pojo.dict;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 字典明细表
 * </p>
 *
 * @author Noah
 * @since 2021-10-18
 */
@Getter
@Setter
@TableName("sys_dict_item")
public class SysDictItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 字典主键
     */
    private String dictId;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典编码
     */
    private String code;

    /**
     * 字典描述
     */
    private String remark;

    /**
     * 父字典项主键
     */
    private String parentId;

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
     * 创建人用户ID
     */
    private String createUserId;

    /**
     * 更新人用户ID
     */
    private String updateUserId;

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
     * 排序字段
     */
    private Integer orderBy;

    /**
     * 删除标志 1正常 0删除
     */
    private String flag;

    /**
     * 搜索码
     */
    private String searchCode;


}
