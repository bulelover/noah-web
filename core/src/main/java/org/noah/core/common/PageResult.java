package org.noah.core.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value="PageResult", description="分页返回公共对象")
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 7413772053921312273L;

    @ApiModelProperty(value = "总条数", position = 0)
    protected long total;
    @ApiModelProperty(value = "数据列表", position = 10)
    private List<T> records;

    public PageResult(long total, List<T> records) {
        this.total = total;
        this.records = records;
    }
    public PageResult() {

    }
}
