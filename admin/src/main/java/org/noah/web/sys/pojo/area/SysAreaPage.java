package org.noah.web.sys.pojo.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.noah.core.common.BasePage;

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
@ApiModel(value = "SysAreaPage对象", description = "行政区域表分页查询入参")
public class SysAreaPage extends BasePage<SysArea> {

    @ApiModelProperty(value = "模糊搜索(生成代码后需再mapper中实现)", position = 0)
    private String search;

    @ApiModelProperty(value = "区域编号", position = 10)
    private String code;

    @ApiModelProperty(value = "区域名称", position = 15)
    private String name;

    @ApiModelProperty(value = "父区域编号", position = 20)
    private String pCode;

    public String getSearch() {
        return this.getLikeSql(search);
    }

}
