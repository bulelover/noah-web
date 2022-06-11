package org.noah.web.sys.service;

import org.noah.core.common.IBaseService;
import org.noah.web.sys.pojo.dict.SysDict;

/**
 * <p>
 * 字典主表 服务类
 * </p>
 *
 * @author Noah
 * @since 2021-10-18
 */
public interface ISysDictService extends IBaseService<SysDict> {

    SysDict selectByCode(String code);
}
