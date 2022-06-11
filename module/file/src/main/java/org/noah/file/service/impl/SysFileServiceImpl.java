package org.noah.file.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.noah.core.common.BaseServiceImpl;
import org.noah.core.exception.BusinessException;
import org.noah.core.utils.BeanUtils;
import org.noah.core.utils.IDUtils;
import org.noah.core.utils.ImageUtils;
import org.noah.file.mapper.SysFileMapper;
import org.noah.file.pojo.file.SysFile;
import org.noah.file.pojo.file.SysFilePage;
import org.noah.file.pojo.file.SysFileVO;
import org.noah.file.service.IFileStorageService;
import org.noah.file.service.ISysFileService;
import org.noah.file.utils.FileUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 文件信息表 服务实现类
 * </p>
 *
 * @author Noah
 * @since 2022-01-22
 */
@Service
public class SysFileServiceImpl extends BaseServiceImpl<SysFileMapper, SysFile> implements ISysFileService {

    private final IFileStorageService fileStorageService;

    public SysFileServiceImpl(IFileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Override
    protected String getOrderTableAlias(String column, Boolean isColumn) {
        if(isColumn){
            return "t."+column;
        }
        return null;
    }


    /**
     * <p>预览文件</p>
     * @param fileId   上传文件ID
     * @param response 请求响应
     */
    @Override
    public void previewFile(String fileId, HttpServletResponse response) {
        try {
            SysFile uploadFile = this.getById(fileId);
            if (null != uploadFile && "1".equals(uploadFile.getFlag())) {
                String filePath = uploadFile.getPath();
                fileStorageService.preview(uploadFile.getName(), filePath, uploadFile.getContentType(), response);
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    /**
     * <p>下载文件</p>
     * @param fileId   上传文件ID
     * @param response 请求响应
     */
    @Override
    public void downLoadFile(String fileId, HttpServletResponse response) {
        try {
            SysFile uploadFile = this.getById(fileId);
            if (null != uploadFile && "1".equals(uploadFile.getFlag())) {
                String filePath = uploadFile.getPath();
                fileStorageService.download(uploadFile.getName(), filePath, uploadFile.getContentType(), response);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
//            e.printStackTrace();
        }
    }


    @Override
    @Transactional(rollbackFor=Exception.class)
    public SysFileVO saveFileInfo(MultipartFile file, HttpServletRequest request, boolean compress){
        String linkName = request.getParameter("linkName");
        String linkId = request.getParameter("linkId");
        String fileName = request.getParameter("fileName");
        boolean single = "1".equals(request.getParameter("single"));
        String contextPath = null;
        MultipartFile newFile = null;
        try {
            if (!file.isEmpty()) {
                String fileMd5 =  DigestUtils.md5DigestAsHex(file.getInputStream());
                SysFile exist = this.getFirstFileByMd5(fileMd5);
                SysFile f = new SysFile();
                if(StringUtils.isBlank(fileName)){
                    fileName = file.getOriginalFilename();
                    // Check for Unix-style path
                    assert fileName != null;
                    int unixSep = fileName.lastIndexOf('/');
                    // Check for Windows-style path
                    int winSep = fileName.lastIndexOf('\\');
                    // Cut off at latest possible point
                    int pos = (Math.max(winSep, unixSep));
                    if (pos != -1)  {
                        // Any sort of path separator found...
                        fileName = fileName.substring(pos + 1);
                    }
                }
                String extension = FilenameUtils.getExtension(fileName);
                //文件不存在保存并返回fileId
                if (exist == null) {
                    if (compress) {
                        String[] date = DateUtil.format(new Date(), "yyyy-MM").split("-");
                        String filePath =  File.separator + date[0] + File.separator + date[1];
                        File directory = new File( FileUtils.createPath() + filePath);
                        if (!directory.exists()) {
                            directory.mkdirs();
                        }
                        String storageFileName =  File.separator + IDUtils.generateUUID() + "." + extension;
                        contextPath = directory.getAbsolutePath()+storageFileName;
                        boolean isPress = (ImageUtils.isImage(file.getInputStream())) && file.getSize() / 1024 > 1024;
                        if (isPress) {
                            //文件压缩写入
                            Thumbnails.of(file.getInputStream()).scale(0.2f).toFile(contextPath);
                            //压缩完成后将缩略图生成MultipartFile
                            FileInputStream fileInputStream = new FileInputStream(contextPath);
                            newFile = new MockMultipartFile(fileName ,fileName, "application/octet-stream", fileInputStream);
                        }
                    }
                    String upFilePath = fileStorageService.upload(newFile==null?file:newFile);
                    //保存file表信息
                    f.setSize(file.getSize());
                    f.setPath(upFilePath);
                } else {
                    //存在则新增一条上传记录即可。
                    //保存file表信息
                    f.setSize(exist.getSize());
                    f.setPath(exist.getPath());
                }
                f.setMd5(fileMd5);
                f.setLinkId(linkId);
                f.setLinkName(linkName);
                f.setFlag("1");
                f.setContentType(file.getContentType());
                f.setType(extension);
                f.setName(fileName);
                //单文件模式，标记删除原来的文件
                if(single && StringUtils.isNotBlank(linkId)){
                    this.removeLogicByLinkId(linkId);
                }
                //产生新文件
                super.create(f);
                return BeanUtils.copyObject(f, SysFileVO.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("上传文件失败，"+e.getMessage());
        }finally {
            if(StringUtils.isNotBlank(contextPath)){
                File defile = new File(contextPath);
                if (defile.exists()) {
                    boolean bool = defile.delete();
                    if(bool){
                        throw new BusinessException("上传文件时，创建文件夹失败");
                    }
                }
            }

        }
        return null;
    }

    private SysFile getFirstFileByMd5(String md5){
        SysFilePage page = new SysFilePage();
        page.setMd5(md5);
        page.setSize(1L);
        List<SysFile> res = this.selectList(page);
        return (res == null || res.size() == 0)?null:res.get(0);
    }

    @Override
    public void removeLogicByLinkId(String linkId){
        UpdateWrapper<SysFile> wrapper = new UpdateWrapper<>();
        wrapper.eq("link_id", linkId);
        //只需要删除未删除的文件即可
        wrapper.eq("flag", "1");
        this.removeLogicByWrapper(wrapper);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void setFileLink(String linkId, String fileId){
        this.setFileLink(true, linkId, fileId);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void setFileLink(String linkId, String... fileIds){
        this.setFileLink(true, linkId, fileIds);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void setFileLink(boolean isDefOld, String linkId, String fileId){
        if(isDefOld){
            this.removeLogicByLinkId(linkId);
        }
        UpdateWrapper<SysFile> wrapper = new UpdateWrapper<>();
        //要改变的值
        wrapper.set("link_id", linkId);
        //条件
        wrapper.eq("id", fileId);
        this.update(wrapper);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void setFileLink(boolean isDefOld, String linkId, String... fileIds){
        if(isDefOld){
            this.removeLogicByLinkId(linkId);
        }
        for(String fileId: fileIds){
            this.setFileLink(isDefOld, linkId, fileId);
        }
    }

    @Override
    public List<SysFileVO> getFilesByLinkId(String linkId){
        QueryWrapper<SysFile> wrapper = new QueryWrapper<>();
        wrapper.eq("link_id", linkId);
        List<SysFile> list =  this.baseMapper.selectList(wrapper);
        return BeanUtils.parseList(list, SysFileVO.class);
    }
}
