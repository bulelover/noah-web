package org.noah.file.pojo.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.noah.core.common.BasePage;

import java.math.BigDecimal;

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
@ApiModel(value = "SysFilePage对象", description = "文件分页查询对象")
public class SysFilePage extends BasePage<SysFile> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件名", position = 40)
    private String name;

    @ApiModelProperty(value = "MD5", position = 50)
    private String md5;

    @ApiModelProperty(value = "文件类型", position = 70)
    private String type;

    @ApiModelProperty(value = "关联业务名称", position = 70)
    private String linkName;

    public String getName() {
        return this.getLikeSql(name);
    }

    public String getLinkName() {
        return this.getLikeSql(linkName);
    }
}
