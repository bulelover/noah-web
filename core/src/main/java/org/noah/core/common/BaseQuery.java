package org.noah.core.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页 继承类
 * @author Noah_X
 */
@Data
public class BaseQuery implements Serializable {

    private static final int DEFAULT_PAGE_NO = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    @ApiModelProperty(value = "当前页码", position = 91)
    private Integer pageNo;

    @ApiModelProperty(value = "每页条数", position = 92)
    private Integer pageSize;

    @ApiModelProperty(value = "排序字段", position = 93)
    private String pageOrderBy;

    @ApiModelProperty(value = "排序方式（asc/desc）", position = 94)
    private String pageOrderType;

    protected BaseQuery(){
        this.pageNo = DEFAULT_PAGE_NO;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo == null?DEFAULT_PAGE_NO:pageNo;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize == null?DEFAULT_PAGE_SIZE:pageSize;
    }
}
