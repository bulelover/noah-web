package org.noah.web.sys.service;

import org.noah.core.common.IBaseService;
import org.noah.web.sys.pojo.dict.SysDictItem;
import org.noah.web.sys.pojo.dict.SysDictItemVO;

import java.util.List;

/**
 * <p>
 * 字典明细表 服务类
 * </p>
 *
 * @author Noah
 * @since 2021-10-18
 */
public interface ISysDictItemService extends IBaseService<SysDictItem> {

    List<SysDictItemVO> selectByDictId(String dictId);
}
