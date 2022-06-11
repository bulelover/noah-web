package org.noah.web.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.commons.lang3.StringUtils;
import org.noah.core.common.BaseServiceImpl;
import org.noah.core.exception.BusinessException;
import org.noah.core.utils.BeanUtils;
import org.noah.core.cache.SysCache;
import org.noah.web.base.cache.UserCache;
import org.noah.web.sys.mapper.SysMenuMapper;
import org.noah.web.sys.pojo.menu.SysMenu;
import org.noah.web.sys.pojo.menu.SysMenuTreeVO;
import org.noah.web.sys.pojo.role.SysRole;
import org.noah.web.sys.service.ISysMenuService;
import org.noah.web.sys.service.ISysRoleService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private final ISysRoleService roleService;

    public SysMenuServiceImpl(ISysRoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    protected String getOrderTableAlias(String column, Boolean isColumn) {
        if(isColumn){
            return "t."+column;
        }
        if("parentName".equals(column)){
            return "p.name";
        }
        return null;
    }

    @Override
    public boolean create(SysMenu entity){
        boolean res = super.create(entity);
        if (res) {
            clearAllMenuListCache();
            UserCache.removeRoleMenuCache("admin");
        }
        return res;
    }

    /**
     * 菜单更新后，清除相关缓存
     * @param menuId 菜单ID
     */
    private void removeRoleMenuCacheByMenuId(String menuId){
        List<SysRole> roles = this.roleService.selectRolesByMenuId(menuId);
        for(SysRole r : roles){
            UserCache.removeRoleMenuCache(r.getId());
        }
    }

    @Override
    public boolean update(SysMenu entity){
        SysMenu old = getById(entity.getId());
        if(old.getParentId() == null){
            old.setParentId("");
        }
        if(StringUtils.isNotBlank(entity.getParentId()) && !old.getParentId().equals(entity.getParentId())){
            //修改菜单时 校验父级菜单不可以是 本级和下级
            if(entity.getId().equals(entity.getParentId())){
                throw new BusinessException("父级菜单不能和本级相同");
            }
            List<SysMenu> menus = this.getAllMenuList();
            Map<String, List<SysMenu>> children = new HashMap<>();
            for(SysMenu menu : menus){
                List<SysMenu> ms = children.get(StringUtils.isBlank(menu.getParentId())?"root-menu":menu.getParentId());
                if(ms == null){
                    ms = new ArrayList<>();
                }
                ms.add(menu);
                children.put(StringUtils.isBlank(menu.getParentId())?"root-menu":menu.getParentId(), ms);
            }
            if (isChild(children, entity.getId(), entity.getParentId())) {
                throw new BusinessException("父级菜单不能是本级的下级，避免死循环");
            }

        }
        boolean res = super.update(entity);
        if (res) {
            removeRoleMenuCacheByMenuId(entity.getId());
            entity.setCreateLoginName(old.getCreateLoginName());
            entity.setCreateTime(old.getCreateTime());
            entity.setCreateRealName(old.getCreateRealName());
            clearAllMenuListCache();
        }
        return res;
    }

    private boolean isChild(Map<String, List<SysMenu>> children, String entityId, String entityPid){
        List<SysMenu> menus = children.get(entityId);
        if(menus == null){
            return false;
        }
        for(SysMenu m : menus){
            if(m.getId().equals(entityPid)){
                return true;
            }
            if(children.get(m.getId()) != null){
                isChild(children, m.getId(), entityPid);
            }
        }
        return false;
    }

    /**
     * 更新菜单状态
     * @param id 菜单ID
     * @param state 状态
     * @return 是否成功
     */
    private boolean changeState(String id, String state){
        SysMenu entity = new SysMenu();
        entity.setId(id);
        entity.setState(state);
        boolean res = SqlHelper.retBool(baseMapper.updateById(entity));
        if(res){
            removeRoleMenuCacheByMenuId(entity.getId());
            clearAllMenuListCache();
        }
        return res;
    }

    @Override
    public boolean disable(String id){
        return changeState(id,"0");
    }

    @Override
    public boolean enable(String id){
        return changeState(id,"1");
    }

    @Override
    public boolean removeLogicById(Serializable id) {
        boolean res = super.removeLogicById(id);
        if (res) {
            clearAllMenuListCache();
            removeRoleMenuCacheByMenuId(id.toString());
        }
        return res;
    }

    @Override
    public List<SysMenu> getAllMenuList(){
        String cache = SysCache.get("menus");
        List<SysMenu> menus;
        if(StringUtils.isBlank(cache)){
            menus = baseMapper.getAllMenuList();
            SysCache.put("menus", JSON.toJSONString(menus));
        }else{
            menus = JSON.parseArray(SysCache.get("menus"), SysMenu.class);
        }
        return menus;
    }

    /**
     * 菜单发生变化时调用本接口，更新所有菜单缓存
     */
    private void clearAllMenuListCache(){
        SysCache.remove("menus");
    }

    @Override
    public List<SysMenu> selectChildren(String pid){
        return baseMapper.selectChildren(pid);
    }

    @Override
    public List<SysMenuTreeVO> getCacheMenuByRoleId(String roleId){
        List<SysMenuTreeVO> res = UserCache.getRoleMenuCache(roleId);
        if(res == null){
            List<SysMenu> menuList;
            //超管角色获取所有权限
            if("admin".equals(roleId)){
                menuList = getAllMenuList();
            }else {
                menuList = baseMapper.selectMenusByRoleId(roleId);
            }
            if(menuList != null){
                res = BeanUtils.parseList(menuList, SysMenuTreeVO.class);
                UserCache.putRoleMenuCache(roleId, JSON.toJSONString(res));
            }
        }
        return res;
    }

    @Override
    public List<SysMenuTreeVO> getUserMenus(String userId){
        List<String> roles = roleService.getCacheRoleByUserId(userId);
        List<SysMenuTreeVO> menus = new LinkedList<>();
        Map<String, Boolean> map = new HashMap<>();
        for(String roleId : roles){
            List<SysMenuTreeVO> allMenus = this.getCacheMenuByRoleId(roleId);
            if(allMenus != null){
                for(SysMenuTreeVO menu: allMenus){
                    if(map.get(menu.getCode()) == null){
                        map.put(menu.getCode(), true);
//                        if("1".equals(menu.getType())) {
                            menus.add(menu);
//                        }
                    }
                }
            }
        }
        Collections.sort(menus);
        return menus;
    }

    @Override
    public List<String> getUserPermissions(String userId){
        List<String> roles = roleService.getCacheRoleByUserId(userId);
        HashSet<String> set = new HashSet<>(200);
        for(String roleId : roles){
            List<SysMenuTreeVO> allMenus = this.getCacheMenuByRoleId(roleId);
            if(allMenus != null){
                for(SysMenuTreeVO menu: allMenus){
                    set.add(menu.getCode());
                }
            }
        }
        return new ArrayList<>(set);
    }


}
