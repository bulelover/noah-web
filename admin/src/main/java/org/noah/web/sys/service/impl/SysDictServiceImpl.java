package org.noah.web.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.noah.core.common.BaseServiceImpl;
import org.noah.web.base.cache.DictCache;
import org.noah.web.sys.pojo.dict.SysDict;
import org.noah.web.sys.mapper.SysDictMapper;
import org.noah.web.sys.pojo.dict.SysDictPage;
import org.noah.web.sys.service.ISysDictItemService;
import org.noah.web.sys.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final ISysDictItemService sysDictItemService;

    @Autowired
    public SysDictServiceImpl(ISysDictItemService sysDictItemService) {
        this.sysDictItemService = sysDictItemService;
    }

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

    @Override
    public Map<String, Object> getAllDictList() {
        Map<String,Object> res = DictCache.getAllDict();
        if(res != null){
            return res;
        }
        res = new HashMap<>();
        SysDictPage page = new SysDictPage();
        List<SysDict> list = this.selectList(page);
        for(SysDict dict : list){
            res.put(dict.getCode(), sysDictItemService.selectByDictId(dict.getId()));
        }
        DictCache.putAllDict(JSON.toJSONString(res));
        return res;
    }

}
