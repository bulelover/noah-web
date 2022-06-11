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
@ApiModel(value="SysDictItemPage", description="字典项列表分页查询入参")
public class SysDictItemPage extends BasePage<SysDictItem> {

    private static final long serialVersionUID = 7083230669794497591L;

    @ApiModelProperty(value = "字典ID", position = 20)
    private String dictId;

    @ApiModelProperty("字典名称")
    private String name;

    @ApiModelProperty("字典编码")
    private String code;

    @ApiModelProperty("字典描述")
    private String remark;
//
//    @ApiModelProperty("父字典项主键")
//    private String parentId;

    @ApiModelProperty("搜索码")
    private String searchCode;

    public String getCode() {
        return this.getLikeSql(code);
    }

    public String getName() {
        return this.getLikeSql(name);
    }

    public String getRemark() {
        return this.getLikeSql(remark);
    }
}
