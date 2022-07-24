package org.noah.flowable.service;

import org.flowable.engine.repository.Model;
import org.noah.core.common.PageResult;
import org.noah.flowable.pojo.model.FlwModelPage;
import org.noah.flowable.pojo.model.ModelSaveVO;
import org.noah.flowable.pojo.model.ModelVO;

/**
 * 工作流模型设计Service
 *
 * @author Noah
 * @since 2022-07-23
 */
public interface IFlwModelService {

    ModelVO getById(String id);

    /**
     * 分页查询（含列表和count）
     * @param page 参数
     * @return 结果
     */
    PageResult<ModelVO> selectPage(FlwModelPage page);

    String save(ModelSaveVO model);

}
