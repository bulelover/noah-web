package org.noah.core.common;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

public interface IBaseService<T> extends IService<T> {

    /**
     * 分页查询（含列表和count）
     * @param page 参数
     * @return 结果
     */
    PageResult<T> selectPage(BasePage<T> page);

    /**
     * 分页查询 结果转换指定的对象（含列表和count）
     * @param page 参数
     * @return 结果
     */
    <E> PageResult<E> selectPage(BasePage<T> page, Class<E> clazz);

    /**
     * 分页查询列表
     * @param page 参数
     * @return 结果
     */
    List<T> selectList(BasePage<T> page);

    /**
     * 新增信息
     * @param entity 新增的信息对象
     * @return 是否成功
     */
    boolean create(T entity);

    /**
     * 修改信息
     * @param entity 要修改的信息对象
     * @return 是否成功
     */
    boolean update(T entity);

    boolean removeLogicById(Serializable id);

    boolean removeLogicByWrapper(UpdateWrapper<T> wrapper);
}
