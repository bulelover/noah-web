package org.noah.web.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.commons.lang3.StringUtils;
import org.noah.core.common.BaseServiceImpl;
import org.noah.core.exception.BusinessException;
import org.noah.web.base.cache.UserCache;
import org.noah.web.sys.mapper.SysRoleMapper;
import org.noah.web.sys.pojo.role.SysRole;
import org.noah.web.sys.service.ISysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Override
    protected String getOrderTableAlias(String column, Boolean isColumn) {
        if(isColumn){
            return "t."+column;
        }
        return null;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void saveRoleMenu(String roleId, String menuIds){
        this.baseMapper.deleteRoleMenusByRoleId(roleId);
        if(StringUtils.isNotBlank(menuIds)){
            String[] arr  = menuIds.split(",");
            for(String menuId : arr){
                this.baseMapper.insertRoleMenu(roleId, menuId);
            }
        }
        //保存角色授权，清除原有缓存
        UserCache.removeRoleMenuCache(roleId);
    }

    @Override
    public boolean update(SysRole entity) {
        if("admin".equals(entity.getId()) && "0".equals(entity.getState())){
            throw new BusinessException("无法禁用超级管理员角色！");
        }
        return super.update(entity);
    }

    @Override
    public boolean disable(String id){
        if("admin".equals(id)){
            throw new BusinessException("无法禁用超级管理员角色！");
        }
        SysRole entity = new SysRole();
        entity.setId(id);
        entity.setState("0");
        return SqlHelper.retBool(baseMapper.updateById(entity));
    }

    @Override
    public boolean enable(String id){
        SysRole entity = new SysRole();
        entity.setId(id);
        entity.setState("1");
        return SqlHelper.retBool(baseMapper.updateById(entity));
    }

    @Override
    public List<SysRole> selectRolesByMenuId(String menuId){
        return baseMapper.selectRolesByMenuId(menuId);
    }

    @Override
    public List<String> getCacheRoleByUserId(String userId){
        List<String> res = UserCache.getUserRoleCache(userId);
        if(res == null){
            List<SysRole> roles = baseMapper.selectRolesByUserId(userId);
            res = new ArrayList<>();
            if(roles != null){
                for(SysRole r : roles){
                    res.add(r.getId());
                }
                UserCache.putUserRoleCache(userId, JSON.toJSONString(res));
            }
        }
        return res;
    }

    @Override
    public List<SysRole> selectAllRolesByUserId(String userId){
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        wrapper.eq("flag", "1");
        wrapper.eq("state", "1");
        List<SysRole> all = baseMapper.selectList(wrapper);
        if (StringUtils.isBlank(userId)) {
            return all;
        }
        List<String> roleIds = this.getCacheRoleByUserId(userId);
        for(SysRole r : all){
            if(roleIds.contains(r.getId())){
                r.setHas("1");
            }else {
                r.setHas("0");
            }
        }
        return all;
    }

    @Override
    public boolean removeLogicById(Serializable id) {
        if("admin".equals(id)){
            throw new BusinessException("无法删除超级管理员角色！");
        }
        UserCache.clear();
        return super.removeLogicById(id);
    }
}
