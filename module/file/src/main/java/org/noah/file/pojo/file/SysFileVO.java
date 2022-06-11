package org.noah.file.pojo.file;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@ApiModel(value = "SysFileVO对象", description = "文件信息返回对象")
public class SysFileVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键", position = 20)
    private String id;

    @ApiModelProperty(value = "物理路径", position = 30)
    private String path;

    @ApiModelProperty(value = "文件名", position = 40)
    private String name;

    @ApiModelProperty(value = "MD5", position = 50)
    private String md5;

    @ApiModelProperty(value = "文件大小", position = 60)
    private Long size;

    @ApiModelProperty(value = "文件类型", position = 70)
    private String type;

    @ApiModelProperty(value = "内容类型", position = 80)
    private String contentType;
}
