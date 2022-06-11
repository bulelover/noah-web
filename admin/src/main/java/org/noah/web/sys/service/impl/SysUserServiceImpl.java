package org.noah.web.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.commons.lang3.StringUtils;
import org.noah.core.common.BaseServiceImpl;
import org.noah.core.exception.BusinessException;
import org.noah.core.satoken.LoginUser;
import org.noah.core.satoken.TokenUtils;
import org.noah.core.utils.BeanUtils;
import org.noah.core.utils.IDUtils;
import org.noah.core.utils.MD5Utils;
import org.noah.file.service.ISysFileService;
import org.noah.web.base.cache.UserCache;
import org.noah.web.sys.mapper.SysUserMapper;
import org.noah.web.sys.pojo.user.SysUser;
import org.noah.web.sys.service.ISysUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Noah
 * @since 2021-07-31
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    protected String getOrderTableAlias(String column, Boolean isColumn) {
        if(isColumn){
            return "t."+column;
        }
        return null;
    }

    @Override
    public SysUser getByLoginName(String loginName){
        return baseMapper.getByLoginName(loginName);
    }

    @Override
    public boolean removeLogicById(Serializable id) {
        if(isAdmin(id.toString())){
            throw new BusinessException("无法删除管理员用户");
        }
        return super.removeLogicById(id);
    }

    @Override
    public boolean disable(String id, String lockReason){
        if(isAdmin(id)){
            throw new BusinessException("无法锁定管理员用户");
        }
        SysUser entity = new SysUser();
        entity.setId(id);
        entity.setState("0");
        entity.setLockReason(lockReason);
        return SqlHelper.retBool(baseMapper.updateById(entity));
    }

    @Override
    public boolean enable(String id){
        SysUser entity = new SysUser();
        entity.setId(id);
        entity.setState("1");
        entity.setLockReason("");
        return SqlHelper.retBool(baseMapper.updateById(entity));
    }

    @Override
    public boolean create(SysUser entity){
        entity.setId(IDUtils.generate());
        SysUser user = this.getByLoginName(entity.getLoginName());
        if(user != null) {
            throw new BusinessException("用户名["+entity.getLoginName()+"]已存在");
        }
        //设置初始密码
        entity.setLoginPwd(MD5Utils.encodeSalt(entity.getLoginName(), entity.getId()));
        return super.create(entity);
    }
    @Override
    public boolean update(SysUser entity){
        //编辑时不更新loginName
        entity.setLoginName(null);
        boolean res = super.update(entity);
        //修改本人信息，刷新登录缓存
        if(entity.getId().equals(TokenUtils.getLoginUserId())){
            TokenUtils.freshData(BeanUtils.parse(this.getById(entity.getId()), LoginUser.class));
        }
        return res;
    }

    @Override
    public void updateImg(String id, String imgId){
        UpdateWrapper<SysUser> wrapper = new UpdateWrapper<>();
        wrapper.set("head_img_path", imgId);
        wrapper.eq("id", id);
        this.update(wrapper);
        //保存头像刷新缓存
        LoginUser user = TokenUtils.getLoginUser();
        assert user != null;
        user.setHeadImgPath(imgId);
        TokenUtils.freshData(user);
    }

    @Override
    public boolean isAdmin(String userId){
        List<String> adminUserIds = UserCache.getAdminUserIds();
        if(adminUserIds == null){
            adminUserIds = new ArrayList<>();
            List<SysUser> users = baseMapper.getUserByRoleId("admin");
            for(SysUser u: users){
                adminUserIds.add(u.getId());
            }
            UserCache.putAdminUserIds(adminUserIds);;
        }
        boolean bool = false;
        for(String id : adminUserIds){
            if (id.equals(userId)) {
                bool = true;
                break;
            }
        }
        return bool;
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void saveUserRole(String userId, String roleIds) {
        if(isAdmin(userId)){
            List<String> adminUserIds = UserCache.getAdminUserIds();
            if(StringUtils.isBlank(roleIds) || !Arrays.asList(roleIds.split(",")).contains("admin")){
                assert adminUserIds != null;
                if(adminUserIds.size() == 1){
                    throw new BusinessException("至少要保留一个用户拥有管理员角色");
                }
            }
        }
        this.baseMapper.deleteUserRolesByUserId(userId);
        if(StringUtils.isNotBlank(roleIds)){
            String[] arr  = roleIds.split(",");
            for(String roleId : arr){
                this.baseMapper.insertUserRole(userId, roleId);
            }
        }
        //保存角色授权，清除原有缓存
        UserCache.removeUserRoleCache(userId);
        UserCache.removeAdminUserIds();
    }

}
