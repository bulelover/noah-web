package org.noah.core.common;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.noah.core.satoken.LoginUser;
import org.noah.core.satoken.TokenUtils;
import org.noah.core.utils.BaseUtils;
import org.noah.core.utils.BeanUtils;
import org.noah.core.utils.IDUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 公共Service 方便后期扩展
 * @author Noah_X
 * @param <M> mapper
 * @param <T> entity
 */

public abstract class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M,T> implements IBaseService<T> {
    @Override
    public PageResult<T> selectPage(BasePage<T> page){
        page.setOrderSql(getOrderSql(page.getOrders()));
        page = this.baseMapper.selectPageList(page);
        return new PageResult<>(page.getTotal(), selectList(page));
    }

    @Override
    public <E> PageResult<E> selectPage(BasePage<T> page, Class<E> clazz){
        page.setOrderSql(getOrderSql(page.getOrders()));
        page = this.baseMapper.selectPageList(page);
        PageResult<E> resPage = new PageResult<>();
        resPage.setTotal(page.getTotal());
        if(page.getTotal() == 0){
            return resPage;
        }
        resPage.setRecords(BeanUtils.parseList(page.getRecords(), clazz));
        return resPage;
    }

    @Override
    public List<T> selectList(BasePage<T> page){
        page.setSearchCount(false);
        page.setMaxLimit(10000L);
        page.setOrderSql(getOrderSql(page.getOrders()));
        return baseMapper.selectPageList(page).getRecords();
    }

    /**
     * 实现此方法返回带有别名的字段即可 例如传入的是 login_name, 可以经过处理为 u.login_name
     * 如果无需处理 则直接 return column; 即可
     * @param column 字段
     * @return 处理过后的字段
     */
    protected abstract String getOrderTableAlias(String column, Boolean isColumn);

    /**
     * 根据entity映射关系动态获取排序sql，同时可以防止sql注入
     * @return 排序sql  eg: ' order by column1 asc, column2 desc ...'
     */
    private String getOrderSql(List<OrderItem> list) {
        String sql = "";
        String alias;
        for(OrderItem item : list){
//            String columnName = BeanUtils.getColumnName(item.getColumn(), entityClass);
            String columnName = BaseUtils.camel2under(item.getColumn());
            if(StringUtils.isNotBlank(columnName) ){
                alias = getOrderTableAlias(columnName, true);
                sql += ", "+alias + ("desc".equals(item.getType())?" desc":" asc");
            }else{
                alias = getOrderTableAlias(item.getColumn(), false);
                if(StringUtils.isNotBlank(alias)){
                    sql += ", "+alias + ("desc".equals(item.getType())?" desc":" asc");
                }
            }
        }
        if("".equals(sql)){
            return sql;
        }
        sql = " order by" + sql.substring(1);
        return sql;
    }

    @Override
    public boolean create(T entity){
        if(entity == null){
            return false;
        }
        LoginUser user = TokenUtils.getLoginUser();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
        BeanUtils.setValue(entity, "createTime", LocalDateTime.now());
        assert user != null;
        BeanUtils.setValue(entity, "createUserId", user.getId());
        BeanUtils.setValue(entity, "createLoginName", user.getLoginName());
        BeanUtils.setValue(entity, "createRealName", user.getRealName());
        //新增（如果没有设置ID，则自动赋予）
        if(StringUtils.isBlank((String)BeanUtils.getValue(entity, tableInfo.getKeyProperty()))){
            BeanUtils.setValue(entity, tableInfo.getKeyProperty(), IDUtils.generate());
        }
        return super.save(entity);
    }

    @Override
    public boolean update(T entity){
        if(entity == null){
            return false;
        }
        LoginUser user = TokenUtils.getLoginUser();
        BeanUtils.setValue(entity, "updateTime", LocalDateTime.now());
        assert user != null;
        BeanUtils.setValue(entity, "updateUserId", user.getId());
        BeanUtils.setValue(entity, "updateLoginName", user.getLoginName());
        BeanUtils.setValue(entity, "updateRealName", user.getRealName());
        return super.updateById(entity);
    }

    /**
     * 重新 实现逻辑删除
     * 如果主键或删除字段不一致，在各service中单独重写即可
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean removeLogicById(Serializable id){
        UpdateWrapper<T> wrapper = new UpdateWrapper<>();
        wrapper.set("flag", "0").eq("id", id);
        return this.update(wrapper);
    }

    /**
     * 重新 实现逻辑删除
     * @param wrapper 参数
     * @return 是否成功
     */
    @Override
    public boolean removeLogicByWrapper(UpdateWrapper<T> wrapper){
        wrapper.set("flag", "0");
        return this.update(wrapper);
    }


}
