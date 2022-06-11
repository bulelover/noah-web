package org.noah.core.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.noah.core.annotation.IgnoreSwaggerParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * 分页 继承类
 * @author Noah_X
 */
@Data
@ApiModel(value="BasePage", description="分页查询公共对象")
public class BasePage<T> implements IPage<T> {

    private static final long serialVersionUID = 8545996863226528798L;
    @IgnoreSwaggerParameter
    protected List<T> records;
    @IgnoreSwaggerParameter
    protected long total;
    @ApiModelProperty(value = "每页条数", position = 9990)
    protected long size;
    @ApiModelProperty(value = "当前页码", position = 9991)
    protected long current;
    @ApiModelProperty(value = "排序字段")
    protected List<OrderItem> orders;
    @IgnoreSwaggerParameter
    protected boolean optimizeCountSql;
    @IgnoreSwaggerParameter
    protected boolean searchCount;
    @IgnoreSwaggerParameter
    protected String countId;
    @IgnoreSwaggerParameter
    protected Long maxLimit;
    @IgnoreSwaggerParameter
    private String orderSql;

    @SuppressWarnings(value = "unchecked")
    public BasePage() {
        this.records = Collections.emptyList();
        this.total = 0L;
        this.size = 10L;
        this.current = 1L;
        this.orders = new ArrayList<>();
        this.optimizeCountSql = true;
        this.searchCount = true;
        this.maxLimit = 500L;
    }

    public BasePage(long current, long size) {
        this(current, size, 0L);
    }

    public BasePage(long current, long size, long total) {
        this(current, size, total, true);
    }

    public BasePage(long current, long size, boolean searchCount) {
        this(current, size, 0L, searchCount);
    }

    public BasePage(long current, long size, long total, boolean searchCount) {
        this.records = Collections.emptyList();
        this.total = 0L;
        this.size = 10L;
        this.current = 1L;
        this.orders = new ArrayList<>();
        this.optimizeCountSql = true;
        this.searchCount = true;
        if (current > 1L) {
            this.current = current;
        }

        this.size = size;
        this.total = total;
        this.searchCount = searchCount;
    }

    public boolean hasPrevious() {
        return this.current > 1L;
    }

    public boolean hasNext() {
        return this.current < this.getPages();
    }

    public List<T> getRecords() {
        return this.records;
    }

    public BasePage<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    public long getTotal() {
        return this.total;
    }

    public BasePage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    public long getSize() {
        return this.size;
    }

    public BasePage<T> setSize(long size) {
        this.size = size;
        return this;
    }

    public long getCurrent() {
        return this.current;
    }

    public BasePage<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    public String countId() {
        return this.countId;
    }

    public Long maxLimit() {
        return this.maxLimit;
    }

    private String[] mapOrderToArray(Predicate<OrderItem> filter) {
        List<String> columns = new ArrayList<>(this.orders.size());
        this.orders.forEach((i) -> {
            if (filter.test(i)) {
                columns.add(i.getColumn());
            }

        });
        return (String[])columns.toArray(new String[0]);
    }

    private void removeOrder(Predicate<OrderItem> filter) {
        for(int i = this.orders.size() - 1; i >= 0; --i) {
            if (filter.test(this.orders.get(i))) {
                this.orders.remove(i);
            }
        }

    }

    public BasePage<T> addOrder(OrderItem... items) {
        this.orders.addAll(Arrays.asList(items));
        return this;
    }

    public BasePage<T> addOrder(List<OrderItem> items) {
        this.orders.addAll(items);
        return this;
    }

    //不使用mybatis-plus的排序（采用xml）
    public List<com.baomidou.mybatisplus.core.metadata.OrderItem> orders() {
        return null;
    }

    public boolean optimizeCountSql() {
        return this.optimizeCountSql;
    }

    public boolean searchCount() {
        return this.total >= 0L && this.searchCount;
    }

    public BasePage<T> setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
        return this;
    }

    public BasePage<T> setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
        return this;
    }

    @ApiModelProperty(hidden = true)
    public long getPages() {
        if (this.getSize() == 0L) {
            return 0L;
        } else {
            long pages = this.getTotal() / this.getSize();
            if (this.getTotal() % this.getSize() != 0L) {
                ++pages;
            }

            return pages;
        }
    }

    public static <T> BasePage<T> of(long current, long size) {
        return of(current, size, 0L);
    }

    public static <T> BasePage<T> of(long current, long size, long total) {
        return of(current, size, total, true);
    }

    public static <T> BasePage<T> of(long current, long size, boolean searchCount) {
        return of(current, size, 0L, searchCount);
    }

    public static <T> BasePage<T> of(long current, long size, long total, boolean searchCount) {
        return new BasePage<>(current, size, total, searchCount);
    }

    public void setOrders(final List<OrderItem> orders) {
        this.orders = orders;
    }

    public void setCountId(final String countId) {
        this.countId = countId;
    }

    public void setMaxLimit(final Long maxLimit) {
        this.maxLimit = maxLimit;
    }

    protected String getLikeSql(String value){
        return StringUtils.isBlank(value)? null: '%'+value+'%';
    }
    protected String getLikeRightSql(String value){
        return StringUtils.isBlank(value)? null: value+'%';
    }
    protected String getLikeLeftSql(String value){
        return StringUtils.isBlank(value)? null: '%'+value;
    }
}
