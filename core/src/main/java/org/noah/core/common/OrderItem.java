package org.noah.core.common;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 排序元素载体
 *
 * @author HCL
 * Create at 2019/5/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="OrderItem", description="排序对象")
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "排序字段", position = 9992)
    private String column;
    @ApiModelProperty(value = "asc正序，desc倒叙，其他无效取数据库默认值", position = 9993)
    private String type;

}
