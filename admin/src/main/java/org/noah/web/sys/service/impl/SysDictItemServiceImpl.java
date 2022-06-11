package org.noah.web.sys.service.impl;

import com.alibaba.fastjson.JSON;
import org.noah.core.common.BaseServiceImpl;
import org.noah.core.utils.BeanUtils;
import org.noah.web.base.cache.DictCache;
import org.noah.web.sys.mapper.SysDictItemMapper;
import org.noah.web.sys.pojo.dict.SysDictItem;
import org.noah.web.sys.pojo.dict.SysDictItemVO;
import org.noah.web.sys.service.ISysDictItemService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 字典明细表 服务实现类
 * </p>
 *
 * @author Noah
 * @since 2021-10-18
 */
@Service
public class SysDictItemServiceImpl extends BaseServiceImpl<SysDictItemMapper, SysDictItem> implements ISysDictItemService {

    @Override
    protected String getOrderTableAlias(String column, Boolean isColumn) {
        if(isColumn){
            return "t."+column;
        }
        return null;
    }

    @Override
    public boolean create(SysDictItem entity) {
        //新增字典项，清除字典缓存
        DictCache.removeItemList(entity.getDictId());
        return super.create(entity);
    }

    @Override
    public boolean update(SysDictItem entity) {
        //更新字典项，清除字典缓存
        DictCache.removeItemList(entity.getDictId());
        return super.update(entity);
    }

    @Override
    public List<SysDictItemVO> selectByDictId(String dictId){
        List<SysDictItemVO> list = DictCache.getItemList(dictId);
        if(list == null){
            List<SysDictItem> list1 = baseMapper.selectByDictId(dictId);
            list = BeanUtils.parseList(list1, SysDictItemVO.class);
            DictCache.putItemList(dictId, JSON.toJSONString(list));
        }
        return list;
    }

}
