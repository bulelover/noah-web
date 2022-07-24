package org.noah.flowable.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.noah.core.annotation.Log;
import org.noah.core.common.BaseController;
import org.noah.core.common.BaseResult;
import org.noah.core.common.PageResult;
import org.noah.core.utils.CheckUtils;
import org.noah.flowable.pojo.model.FlwModelPage;
import org.noah.flowable.pojo.model.ModelSaveVO;
import org.noah.flowable.pojo.model.ModelVO;
import org.noah.flowable.service.IFlwModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/flowable/model")
@RestController
@Api(tags = "流程控制")
@ApiSort(10001)
@Log("流程控制/模型管理")
public class FlwModelController extends BaseController {

    @Autowired
    IFlwModelService iFlwModelService;

    @SaCheckPermission("flw-model-view")
    @GetMapping("/getById")
    @ApiImplicitParam(name = "id", value = "区划ID")
    @ApiOperation(value = "根据ID查询工作流模型")
    @ApiOperationSupport(order = 10)
    public BaseResult<ModelVO> getById(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        ModelVO entity = iFlwModelService.getById(id);
        if(entity == null){
            return this.failure("信息不存在");
        }
        return this.success(entity);
    }

    @SaCheckPermission("flw-model")
    @PostMapping("/page")
    @ApiOperation(value = "工作流模型分页列表查询")
    @ApiOperationSupport(order = 20)
    public BaseResult<PageResult<ModelVO>> page(FlwModelPage page){
        PageResult<ModelVO> res = iFlwModelService.selectPage(page);
        return this.success(res);
    }


    @SaCheckPermission("flw-model-save")
    @PostMapping("/save")
    @ApiOperation(value = "保存工作流模型")
    @ApiOperationSupport(order = 20)
    public BaseResult<String> save(ModelSaveVO vo){
        return this.success(iFlwModelService.save(vo));
    }
}
