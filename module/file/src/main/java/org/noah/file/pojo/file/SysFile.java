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
@ApiModel(value = "SysFile对象", description = "文件信息表")
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("物理路径")
    private String path;

    @ApiModelProperty("文件名")
    private String name;

    @ApiModelProperty("MD5")
    private String md5;

    @ApiModelProperty("文件大小")
    private Long size;

    @ApiModelProperty("文件类型")
    private String type;

    @ApiModelProperty("内容类型")
    private String contentType;

    @ApiModelProperty("业务关联主键")
    private String linkId;

    @ApiModelProperty("业务关联名称")
    private String linkName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人登录用户名")
    private String createLoginName;

    @ApiModelProperty("创建人真实姓名")
    private String createRealName;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("更新人登录用户名")
    private String updateLoginName;

    @ApiModelProperty("更新人真实姓名")
    private String updateRealName;

    @ApiModelProperty("删除标志 1正常 0删除")
    private String flag;

    @ApiModelProperty("排序字段")
    private Integer orderBy;

}
