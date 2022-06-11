package org.noah.web.sys.pojo.dict;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "SysDictItemVO")
public class SysDictItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    private String dictId;

    @ApiModelProperty("字典名称")
    private String name;

    @ApiModelProperty("字典编码")
    private String code;

    @ApiModelProperty("字典描述")
    private String remark;

    @ApiModelProperty("父字典项主键")
    private String parentId;

    @ApiModelProperty("排序字段")
    private Integer orderBy;

    @ApiModelProperty("搜索码")
    private String searchCode;


}
