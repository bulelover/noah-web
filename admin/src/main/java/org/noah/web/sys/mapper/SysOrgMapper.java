package org.noah.web.sys.mapper;

import org.noah.web.sys.pojo.org.SysOrg;
import org.noah.core.common.BaseMapper;

import java.util.List;

/**
 * <p>
 * 组织机构 Mapper 接口
 * </p>
 *
 * @author Noah
 * @since 2022-08-14
 */
public interface SysOrgMapper extends BaseMapper<SysOrg> {

    List<SysOrg> selectChildren(String pid);
}
