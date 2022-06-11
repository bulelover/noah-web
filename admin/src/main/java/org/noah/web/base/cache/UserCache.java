package org.noah.web.base.cache;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.noah.core.utils.CacheUtils;
import org.noah.web.sys.pojo.menu.SysMenuTreeVO;

import java.util.List;

public class UserCache {
    private static final String CACHE_NAME= "userCache";
    private static final String USER_ROLE_= "user_role_";
    private static final String ROLE_MENU_= "role_menu_";
    private static final String ADMIN_USER= "admin-user";

    public static List<String> getAdminUserIds(){
        String res = CacheUtils.get(CACHE_NAME, ADMIN_USER);
        if(StringUtils.isBlank(res)){
            return null;
        }
        return JSON.parseArray(res, String.class);
    }

    public static void putAdminUserIds(List<String> adminUserIds){
        CacheUtils.put(CACHE_NAME, ADMIN_USER, JSON.toJSONString(adminUserIds));
    }

    public static void removeAdminUserIds(){
        CacheUtils.remove(CACHE_NAME, ADMIN_USER);
    }

    public static List<String> getUserRoleCache(String userId){
        String res = CacheUtils.get(CACHE_NAME, USER_ROLE_+userId);
        if(StringUtils.isBlank(res)){
            return null;
        }
        return JSON.parseArray(res, String.class);
    }
    public static void putUserRoleCache(String userId, String value){
        CacheUtils.put(CACHE_NAME, USER_ROLE_+userId, value);
    }
    public static void removeUserRoleCache(String userId){
        CacheUtils.remove(CACHE_NAME, USER_ROLE_+userId);
    }

    public static List<SysMenuTreeVO> getRoleMenuCache(String roleId){
        String res = CacheUtils.get(CACHE_NAME, ROLE_MENU_+roleId);
        if(StringUtils.isBlank(res)){
            return null;
        }
        return JSON.parseArray(res, SysMenuTreeVO.class);
    }
    public static void putRoleMenuCache(String roleId, String value){
        CacheUtils.put(CACHE_NAME, ROLE_MENU_+roleId, value);
    }
    public static void removeRoleMenuCache(String roleId){
        CacheUtils.remove(CACHE_NAME, ROLE_MENU_+roleId);
    }

    public static void clear(){
        CacheUtils.clear(CACHE_NAME);
    }
}
