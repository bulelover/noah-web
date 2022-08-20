package org.noah.web.sys.service;

import org.noah.core.common.IBaseService;
import org.noah.web.sys.pojo.org.SysOrg;

import java.util.List;

/**
 * <p>
 * 组织机构 服务类
 * </p>
 *
 * @author Noah
 * @since 2022-08-14
 */
public interface ISysOrgService extends IBaseService<SysOrg> {

    /**
     * 根据pid查询下级的组织
     * @param pid 上级id
     * @return List<SysOrg>
     */
    List<SysOrg> selectChildren(String pid);

    SysOrg getByCode(String code);

    List<SysOrg> getByTreeIds(String treeIds);
}
