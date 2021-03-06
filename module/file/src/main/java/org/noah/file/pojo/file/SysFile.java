package org.noah.file.pojo.file;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 文件信息表
 * </p>
 *
 * @author Noah
 * @since 2022-01-22
 */
@Getter
@Setter
@TableName("sys_file")
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 物理路径
     */
    private String path;

    /**
     * 文件名
     */
    private String name;

    /**
     * MD5
     */
    private String md5;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * 业务关联主键
     */
    private String linkId;

    /**
     * 业务关联名称
     */
    private String linkName;

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
     * 删除标志 1正常 0删除
     */
    private String flag;

    /**
     * 排序字段
     */
    private Integer orderBy;

}
