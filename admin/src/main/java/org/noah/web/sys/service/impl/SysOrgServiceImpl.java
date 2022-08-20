package org.noah.web.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.commons.lang3.StringUtils;
import org.noah.core.common.BaseServiceImpl;
import org.noah.core.exception.BusinessException;
import org.noah.web.sys.pojo.org.SysOrg;
import org.noah.web.sys.mapper.SysOrgMapper;
import org.noah.web.sys.service.ISysOrgService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 组织机构 服务实现类
 * </p>
 *
 * @author Noah
 * @since 2022-08-14
 */
@Service
public class SysOrgServiceImpl extends BaseServiceImpl<SysOrgMapper, SysOrg> implements ISysOrgService {

    @Override
    protected String getOrderTableAlias(String column, Boolean isColumn) {
        if(isColumn){
            return "t."+column;
        }
        return null;
    }

    @Override
    public List<SysOrg> selectChildren(String pid) {
        return baseMapper.selectChildren(pid);
    }

    @Override
    public boolean create(SysOrg entity) {
        SysOrg org = this.getByCode(entity.getCode());
        if (org != null) {
            throw new BusinessException("（" + entity.getCode() + "）该机构编码已存在");
        }
        //设置TreeIds
        SysOrg parentOrg = this.getById(entity.getParentId());
        if(parentOrg !=null){
            entity.setTreeIds(parentOrg.getTreeIds()+entity.getId()+".");
        }else{
            entity.setTreeIds(entity.getId()+".");
        }
        return super.create(entity);
    }

    @Override
    public boolean update(SysOrg entity) {
        SysOrg oldOrg = this.getById(entity.getId());
        //当编码发生变化时，校验机构编码的重复性
        if(StringUtils.isNotBlank(entity.getCode()) && !oldOrg.getCode().equals(entity.getCode())){
            SysOrg org = this.getByCode(entity.getCode());
            if (org != null) {
                throw new BusinessException("（" + entity.getCode() + "）该机构编码已存在");
            }
        }
        if("0".equals(entity.getState()) && "admin".equals(entity.getId())){
            throw new BusinessException("超管机构不可禁用");
        }

        return updateTree(entity, oldOrg);
    }

    private boolean updateTree(SysOrg entity,  SysOrg oldOrg){
        if(StringUtils.isNotBlank(entity.getParentId())){
            if(entity.getId().equals(entity.getParentId())){
                throw new BusinessException("上级机构不能选择本级机构");
            }
        }
        SysOrg parentOrg = this.getById(entity.getParentId());
        if(parentOrg !=null){
            entity.setTreeIds(parentOrg.getTreeIds()+entity.getId()+".");
        }else{
            entity.setTreeIds(entity.getId()+".");
        }
        String newTree=entity.getTreeIds();
        String oldTree=oldOrg.getTreeIds();
        if(StringUtils.isNotBlank(entity.getParentId())){
            if(!newTree.equals(oldTree) && StringUtils.isNotBlank(oldTree)){
                if(newTree.startsWith(oldTree)){
                    throw new BusinessException("上级机构不允许选择为该机构的下级机构");
                }
            }
        }
        boolean res = super.update(entity);
        //当上级改变后，循环更新其下级的treeIds
        if(res && !entity.getParentId().equals(oldOrg.getParentId())){
            List<SysOrg> orgList= this.getByTreeIds(oldTree);
            if(orgList.size()>0){
                for(SysOrg or : orgList){
                    String oldTreeIds = or.getTreeIds();
                    or.setTreeIds(oldTreeIds.replace(oldTree, newTree));
                    this.updateTreeIdsById(or.getId(), or.getTreeIds());
                }
            }
        }
        return res;
    }

    @Override
    public SysOrg getByCode(String code){
        if(StringUtils.isBlank(code)){
            return null;
        }
        QueryWrapper<SysOrg> wrapper = new QueryWrapper<>();
        wrapper.eq("flag", "1");
        wrapper.eq("code", code);
        return this.getOne(wrapper);
    }

    @Override
    public List<SysOrg> getByTreeIds(String treeIds){
        if(StringUtils.isBlank(treeIds)){
            return null;
        }
        QueryWrapper<SysOrg> wrapper = new QueryWrapper<>();
        wrapper.eq("flag", "1");
        wrapper.likeRight("TREE_IDS", treeIds);
        return this.list(wrapper);
    }

    private void updateTreeIdsById(String id, String treeIds){
        UpdateWrapper<SysOrg> wrapper = new UpdateWrapper<>();
        wrapper.set("TREE_IDS", treeIds);
        wrapper.eq("id", id);
        this.update(wrapper);
    }
}
