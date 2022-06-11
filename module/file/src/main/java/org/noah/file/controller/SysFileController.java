package org.noah.file.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.noah.file.pojo.file.SysFileVO;
import org.noah.file.service.ISysFileService;
import org.noah.core.common.BaseResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import org.noah.core.common.BaseController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 文件信息表 前端控制器
 * </p>
 *
 * @author Noah
 * @since 2022-01-22
 */
@Api(tags = "文件上传")
@ApiSort(901)
@RestController
@RequestMapping("/file")
public class SysFileController extends BaseController {

    private final ISysFileService sysFileService;

    public SysFileController(ISysFileService sysFileService) {
        this.sysFileService = sysFileService;
    }

    @GetMapping(value = "/list/{linkId}")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "查询业务关联文件列表", httpMethod = "GET")
    @ApiImplicitParam(value = "业务主键", name = "linkId", required = true)
    public List<SysFileVO> list(@PathVariable("linkId") String linkId) {
        return this.sysFileService.getFilesByLinkId(linkId);
    }

    /**
     * 上传文件
     * @return 上传文件后的相关属性
     */
    @ApiOperation(value = "上传文件", httpMethod = "POST")
    @ApiOperationSupport(order = 10)
    @ApiImplicitParams({
            @ApiImplicitParam(value = "上传文件", name = "file", paramType = "form", dataType = "__file"),
            @ApiImplicitParam(value = "关联业务名称", name = "linkName", required = true),
            @ApiImplicitParam(value = "自定义文件名称", name = "fileName", required = false),
            @ApiImplicitParam(value = "是否单文件（一个业务只有一个文件，将会标记删除之前绑定的文件）", name = "single", required = false),
            @ApiImplicitParam(value = "是否压缩 1压缩（针对图片,超出1M后压缩）", name = "compress", required = false)
    })
    @SaCheckLogin
    @PostMapping("/upload")
    public BaseResult<SysFileVO> upload(HttpServletRequest request, HttpServletResponse response, MultipartFile file) throws IllegalStateException, IOException {
        String compress = request.getParameter("compress");
        SysFileVO vo = sysFileService.saveFileInfo(file, request, "1".equals(compress));
        return BaseResult.succeed(vo);
    }

    @GetMapping("/preview/{fileId}")
    @ApiOperationSupport(order = 12)
    @ApiOperation(value = "文件预览", httpMethod = "GET")
    @ApiImplicitParam(value = "文件ID", name = "fileId", required = true)
    public void previewFile(@PathVariable("fileId") String fileId, HttpServletResponse response) {
        sysFileService.previewFile(fileId, response);
    }

    @GetMapping(value = "/download/{fileId}")
    @ApiOperationSupport(order = 14)
    @ApiOperation(value = "下载文件", httpMethod = "GET")
    @ApiImplicitParam(value = "文件ID", name = "fileId", required = true)
    public void downloadFile(@PathVariable("fileId") String fileId, HttpServletResponse response) {
        sysFileService.downLoadFile(fileId, response);
    }

}
