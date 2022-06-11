package org.noah.web.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.noah.core.common.BaseServiceImpl;
import org.noah.web.base.cache.DictCache;
import org.noah.web.sys.pojo.dict.SysDict;
import org.noah.web.sys.mapper.SysDictMapper;
import org.noah.web.sys.service.ISysDictService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典主表 服务实现类
 * </p>
 *
 * @author Noah
 * @since 2021-10-18
 */
@Service
public class SysDictServiceImpl extends BaseServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Override
    protected String getOrderTableAlias(String column, Boolean isColumn) {
        if(isColumn){
            return "t."+column;
        }
        return null;
    }

    @Override
    public boolean update(SysDict entity) {
        SysDict old = baseMapper.selectById(entity.getId());
        if(!entity.getCode().equals(old.getCode())){
            //编码发生变化，清除原有的缓存
            DictCache.removeDictIdByCode(old.getCode());
        }
        return super.update(entity);
    }

    @Override
    public SysDict selectByCode(String code){
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();
        wrapper.eq("flag", "1").eq("code", code);
        SysDict dict = baseMapper.selectOne(wrapper);
        DictCache.putDictIdByCode(code, dict.getId());
        return dict;
    }

}
