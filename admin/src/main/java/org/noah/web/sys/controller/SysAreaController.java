package org.noah.web.sys.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
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
import org.noah.web.sys.pojo.area.SysArea;
import org.noah.web.sys.pojo.area.SysAreaListVO;
import org.noah.web.sys.pojo.area.SysAreaPage;
import org.noah.web.sys.pojo.area.SysAreaVO;
import org.noah.web.sys.service.ISysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 行政区域表 前端控制器
 * </p>
 *
 * @author Noah
 * @since 2022-06-26
 */
@Api(tags = "区划管理")
@ApiSort(110)
@Log("系统管理/区划管理")
@RestController
@RequestMapping("/sys/area")
public class SysAreaController extends BaseController {

    private final ISysAreaService sysAreaService;

    @Autowired
    public SysAreaController(ISysAreaService sysAreaService) {
        this.sysAreaService = sysAreaService;
    }

    @SaCheckPermission("sys-area-view")
    @GetMapping("/getById")
    @ApiImplicitParam(name = "id", value = "区划ID")
    @ApiOperation(value = "根据ID查询区划")
    @ApiOperationSupport(order = 10)
    public BaseResult<SysAreaListVO> getById(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        SysArea entity = sysAreaService.getById(id);
        if(entity == null || "0".equals(entity.getFlag())){
            return this.failure("信息不存在");
        }
        return this.success(BeanUtils.parse(entity, SysAreaListVO.class));
    }

    @SaCheckPermission("sys-area")
    @PostMapping("/page")
    @ApiOperation(value = "区划分页列表查询")
    @ApiOperationSupport(order = 20)
    public BaseResult<PageResult<SysAreaListVO>> page(SysAreaPage page){
        PageResult<SysAreaListVO> res = sysAreaService.selectPage(page,  SysAreaListVO.class);
        return this.success(res);
    }

    @SaCheckPermission("sys-area")
    @GetMapping("/children")
    @ApiOperation(value = "查询下级区划列表")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(name = "pid",value = "上级区划ID")
    public BaseResult<List<SysAreaListVO>> selectChildren(@RequestParam(required = false) String pid){
        List<SysArea> res = sysAreaService.selectChildren(pid);
        return this.success(BeanUtils.parseList(res, SysAreaListVO.class));
    }

    @SaCheckPermission("sys-area-add")
    @Log("新增")
    @ApiOperation(value = "新增区划信息")
    @ApiOperationSupport(order = 30, ignoreParameters = {"id"})
    @PostMapping("/add")
    public BaseResult<String> add(SysAreaVO vo){
        //校验参数, id不用传
        CheckUtils.checkExcludeFields(vo, "id").checkError();
        //id与code保持一致并唯一
        vo.setId(vo.getCode());
        SysArea entity = BeanUtils.parse(vo, SysArea.class);
        return this.result(this.sysAreaService.create(entity), entity.getId());
    }

    @SaCheckPermission("sys-area-edit")
    @Log("修改")
    @ApiOperation(value = "修改区划信息")
    @ApiOperationSupport(order = 36)
    @PostMapping("/edit")
    public BaseResult<String> edit(SysAreaVO vo){
        //校验参数
        CheckUtils.checkAllFields(vo).checkError();
        SysArea entity = BeanUtils.parse(vo, SysArea.class);
        return this.result(this.sysAreaService.update(entity), entity.getId());
    }

    @SaCheckPermission("sys-area-delete")
    @Log("删除")
    @ApiOperation(value = "删除区划")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id",value = "区划ID", required = true)
    })
    @ApiOperationSupport(order = 40)
    @PostMapping("/removeById")
    public BaseResult<String> removeById(@RequestParam(required = false) String id){
        //校验ID不可为空
        CheckUtils.init().set(id, "ID").required().checkError();
        //校验是否有下级，有下级不可删除
        List<SysArea> res = sysAreaService.selectChildren(id);
        if(res.size()>0){
            return this.failure("存在下级地区，不可删除！");
        }
        //物理删除
        return this.result(this.sysAreaService.removeById(id));
    }

}
