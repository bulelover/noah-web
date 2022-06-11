package org.noah.web.sys.pojo.dict;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.noah.core.common.BasePage;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysDictPage", description="字典列表分页查询入参")
public class SysDictPage extends BasePage<SysDict> {

    private static final long serialVersionUID = 7083230669794497591L;

    @ApiModelProperty(value = "字典名称", position = 20)
    private String name;

    @ApiModelProperty(value = "字典编码", position = 30)
    private String code;

    @ApiModelProperty(value = "编码/名称", position = 30)
    private String search;

    @ApiModelProperty(value = "字典描述", position = 30)
    private String remark;

    public String getCode() {
        return this.getLikeSql(code);
    }

    public String getSearch() {
        return this.getLikeSql(search);
    }

    public String getName() {
        return this.getLikeSql(name);
    }

    public String getRemark() {
        return this.getLikeSql(remark);
    }
}
