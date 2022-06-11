package org.noah.core.common;

public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    BasePage<T> selectPageList(BasePage<T> page);

}
