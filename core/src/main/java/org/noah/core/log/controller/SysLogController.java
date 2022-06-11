package org.noah.core.log.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.noah.core.common.BaseController;
import org.noah.core.common.BaseResult;
import org.noah.core.common.PageResult;
import org.noah.core.log.pojo.log.SysLog;
import org.noah.core.log.pojo.log.SysLogPage;
import org.noah.core.log.pojo.log.SysRequest;
import org.noah.core.log.pojo.log.SysRequestPage;
import org.noah.core.log.service.ISysLogService;
import org.noah.core.log.service.ISysRequestService;
import org.noah.core.satoken.TokenUtils;
import org.noah.core.utils.CheckUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @author Noah
 * @since 2021-08-03
 */
@Api(tags = "日志管理")
@ApiSort(905)
@RestController
@RequestMapping("/sys/log")
public class SysLogController extends BaseController {

    private final ISysLogService sysLogService;
    private final ISysRequestService sysRequestService;

    public SysLogController(ISysLogService sysLogService, ISysRequestService sysRequestService) {
        this.sysLogService = sysLogService;
        this.sysRequestService = sysRequestService;
    }

    @SaCheckPermission("sys-log-view")
    @GetMapping("/getById")
    @ApiImplicitParam(name = "id",value = "日志ID")
    @ApiOperation(value = "根据ID查询日志")
    @ApiOperationSupport(order = 10)
    public BaseResult<SysLog> getById(@RequestParam(required = false) String id){
        CheckUtils.init().set(id, "ID").required().checkError();
        SysLog entity = sysLogService.getById(id);
        if(entity == null || "0".equals(entity.getFlag())){
            return this.failure("信息不存在");
        }
        return this.success(entity);
    }

    @SaCheckPermission("sys-log")
    @PostMapping("/page")
    @ApiOperation(value = "日志分页列表查询")
    @ApiOperationSupport(order = 20)
    public BaseResult<PageResult<SysLog>> page(SysLogPage page){
        PageResult<SysLog> res = sysLogService.selectPage(page);
        return this.success(res);
    }

    @SaCheckPermission("sys-log-my")
    @PostMapping("/myPage")
    @ApiOperation(value = "日志分页列表查询")
    @ApiOperationSupport(order = 20)
    public BaseResult<PageResult<SysLog>> myPage(SysLogPage page){
        page.setCreateLoginName(TokenUtils.getLoginName());
        PageResult<SysLog> res = sysLogService.selectPage(page);
        return this.success(res);
    }

    @SaCheckPermission("sys-request")
    @PostMapping("/requestPage")
    @ApiOperation(value = "访问日志分页列表查询")
    @ApiOperationSupport(order = 20)
    public BaseResult<PageResult<SysRequest>> page(SysRequestPage page){
        //默认不包含“访问日志分页列表查询”本身的请求
        if(StringUtils.isBlank(page.getOwner())){
            page.setOwner("1");
        }
        PageResult<SysRequest> res = sysRequestService.selectPage(page);
        return this.success(res);
    }

}
