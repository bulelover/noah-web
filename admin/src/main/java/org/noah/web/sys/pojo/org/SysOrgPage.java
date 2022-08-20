package org.noah.web.sys.pojo.org;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.noah.web.sys.pojo.org.SysOrg;
import org.noah.core.common.BasePage;

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
@ApiModel(value = "SysOrgPage对象", description = "组织机构分页查询入参")
public class SysOrgPage extends BasePage<SysOrg> {

    @ApiModelProperty(value = "模糊搜索", position = 0)
    private String search;

    @ApiModelProperty(value = "组织机构代码", position = 10)
    private String code;

    @ApiModelProperty(value = "组织/企业/单位名称", position = 15)
    private String name;

    @ApiModelProperty(value = "组织类型", position = 20)
    private String type;

    @ApiModelProperty(value = "层级ID", position = 20)
    private String treeIds;

    public String getSearch() {
        return this.getLikeSql(search);
    }

    public String getTreeIds() {
        return this.getLikeRightSql(treeIds);
    }
}
