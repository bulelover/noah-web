package org.noah.web.sys.service.impl;

import org.noah.core.common.BaseServiceImpl;
import org.noah.web.sys.pojo.org.SysOrg;
import org.noah.web.sys.mapper.SysOrgMapper;
import org.noah.web.sys.service.ISysOrgService;
import org.springframework.stereotype.Service;

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

}
