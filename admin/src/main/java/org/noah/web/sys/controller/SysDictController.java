package org.noah.web.sys.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.fastjson.JSON;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.noah.core.annotation.Log;
import org.noah.core.common.BaseController;
import org.noah.core.common.BaseResult;
import org.noah.core.common.PageResult;
import org.noah.core.utils.BeanUtils;
import org.noah.core.utils.CheckUtils;
import org.noah.web.base.cache.DictCache;
import org.noah.web.sys.pojo.dict.*;
import org.noah.web.sys.service.ISysDictItemService;
import org.noah.web.sys.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典主表 前端控制器
 * </p>
 *
 * @author Noah
 * @since 2021-10-18
 */
@Api(tags = "字典管理")
@RestController
@Log("系统管理/字典管理")
@ApiSort(110)
@RequestMapping("/sys/dict")
public class SysDictController extends BaseController {

    private final ISysDictService sysDictService;
    private final ISysDictItemService sysDictItemService;

    @Autowired
    public SysDictController(ISysDictService sysDictService, ISysDictItemService sysDictItemService) {
        this.sysDictService = sysDictService;
        this.sysDictItemService = sysDictItemService;
    }

    @GetMapping("/getAllDictList")
    @ApiOperation(value = "查询所有字典信息")
    @ApiOperationSupport(order = 1)
    public BaseResult<Map<String,Object>> getAllDictList(){
        return this.success(sysDictService.getAllDictList());
    }

    @SaCheckPermission("sys-dict-view")
    @GetMapping("/getById")
    @ApiImplicitParam(name = "id",value = "字典ID")
    @ApiOperation(value = "根据ID查询字典")
    @ApiOperationSupport(order = 10)
    public BaseResult<SysDict> getById(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        SysDict entity = sysDictService.getById(id);
        if(entity == null || "0".equals(entity.getFlag())){
            return this.failure("信息不存在");
        }
        return this.success(entity);
    }

    @SaCheckPermission("sys-dict")
    @PostMapping("/page")
    @ApiOperation(value = "字典分页列表查询")
    @ApiOperationSupport(order = 20)
    public BaseResult<PageResult<SysDict>> page(SysDictPage page){
        PageResult<SysDict> res = sysDictService.selectPage(page);
        return this.success(res);
    }

    @SaCheckPermission("sys-dict-add")
    @Log("新增")
    @ApiOperation(value = "新增字典信息")
    @ApiOperationSupport(order = 30, ignoreParameters = {"id"})
    @PostMapping("/save")
    public BaseResult<String> add(SysDictVO vo){
        //校验参数,id不用传
        CheckUtils.checkExcludeFields(vo, "id").checkError();
        SysDict entity = BeanUtils.parse(vo, SysDict.class);
        return this.result(this.sysDictService.create(entity), entity.getId());
    }

    @SaCheckPermission("sys-dict-edit")
    @Log("修改")
    @ApiOperation(value = "修改字典信息")
    @ApiOperationSupport(order = 31)
    @PutMapping("/save")
    public BaseResult<String> edit(SysDictVO vo){
        //校验参数
        CheckUtils.checkAllFields(vo).checkError();
        SysDict entity = BeanUtils.parse(vo, SysDict.class);
        DictCache.removeAllDict();
        return this.result(this.sysDictService.update(entity), entity.getId());
    }
    @SaCheckPermission("sys-dict-delete")
    @Log("删除字典")
    @ApiOperation(value = "删除字典信息")
    @ApiOperationSupport(order = 32)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "字典ID", required = true)
    })
    @DeleteMapping("/removeById")
    public BaseResult<String> removeById(String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        DictCache.removeAllDict();
        return this.result(this.sysDictService.removeLogicById(id));
    }

    @SaCheckPermission("sys-dict-item")
    @GetMapping("/getItemById")
    @ApiImplicitParam(name = "id",value = "字典项ID")
    @ApiOperation(value = "根据ID查询字典项")
    @ApiOperationSupport(order = 50)
    public BaseResult<SysDictItem> getItemById(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        SysDictItem entity = sysDictItemService.getById(id);
        if(entity == null || "0".equals(entity.getFlag())){
            return this.failure("信息不存在");
        }
        return this.success(entity);
    }

    @SaCheckPermission("sys-dict")
    @PostMapping("/dictItemPage")
    @ApiOperation(value = "根据字典ID查询字典项列表")
    @ApiOperationSupport(order = 52)
    public BaseResult<PageResult<SysDictItem>> dictItemPage(SysDictItemPage page){
        CheckUtils.init().set(page.getDictId(), "字典ID").required().checkError();
        return this.success(sysDictItemService.selectPage(page));
    }

    @SaCheckPermission("sys-dict-item-add")
    @Log("新增字典项")
    @ApiOperation(value = "新增字典项信息")
    @ApiOperationSupport(order = 54, ignoreParameters = {"id"})
    @PostMapping("/saveItem")
    public BaseResult<String> add(SysDictItemVO vo){
        //校验参数,id不用传
        CheckUtils.checkExcludeFields(vo, "id").checkError();
        SysDictItem entity = BeanUtils.parse(vo, SysDictItem.class);
        DictCache.removeAllDict();
        return this.result(this.sysDictItemService.create(entity), entity.getId());
    }

    @SaCheckPermission("sys-dict-item-edit")
    @Log("修改字典项")
    @ApiOperation(value = "修改字典项信息")
    @ApiOperationSupport(order = 56)
    @PutMapping("/saveItem")
    public BaseResult<String> edit(SysDictItemVO vo){
        //校验参数
        CheckUtils.checkAllFields(vo).checkError();
        SysDictItem entity = BeanUtils.parse(vo, SysDictItem.class);
        DictCache.removeAllDict();
        return this.result(this.sysDictItemService.update(entity), entity.getId());
    }

    @SaCheckPermission("sys-dict-item-delete")
    @Log("删除字典项")
    @ApiOperation(value = "删除字典项信息")
    @ApiOperationSupport(order = 57)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "字典项ID", required = true)
    })
    @DeleteMapping("/removeByItemId")
    public BaseResult<String> removeByItemId(String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        DictCache.removeAllDict();
        return this.result(this.sysDictItemService.removeLogicById(id));
    }
}
