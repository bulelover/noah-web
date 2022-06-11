package org.noah.file.service;

import org.noah.core.common.IBaseService;
import org.noah.file.pojo.file.SysFile;
import org.noah.file.pojo.file.SysFileVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 文件信息表 服务类
 * </p>
 *
 * @author Noah
 * @since 2022-01-22
 */
public interface ISysFileService extends IBaseService<SysFile> {

    void previewFile(String fileId, HttpServletResponse response);

    void downLoadFile(String fileId, HttpServletResponse response);

    /**
     * 上传文件接口
     * @param file 文件
     * @param request HTTP请求
     * @param compress 是否压缩
     * @return 上传成功的信息
     */
    @Transactional(rollbackFor=Exception.class)
    SysFileVO saveFileInfo(MultipartFile file, HttpServletRequest request, boolean compress);

    /**
     * 根据关联业务主键逻辑删除文件
     * @param linkId 关联业务主键
     */
    void removeLogicByLinkId(String linkId);

    /**
     * 设置file业务关联关系，默认会删除原有的关联关系,再重新建立新的关系
     * @param linkId 业务关联ID
     * @param fileId 文件ID
     */
    @Transactional(rollbackFor=Exception.class)
    void setFileLink(String linkId, String fileId);

    /**
     * 设置file业务关联关系，默认会删除原有的关联关系,再重新建立新的关系
     * @param linkId 业务关联ID
     * @param fileIds 文件ID
     */
    @Transactional(rollbackFor=Exception.class)
    void setFileLink(String linkId, String... fileIds);

    /**
     * 设置file业务关联关系
     * @param isDefOld 是否删除原有关联关系
     * @param linkId 业务关联ID
     * @param fileId 文件ID
     */
    @Transactional(rollbackFor=Exception.class)
    void setFileLink(boolean isDefOld, String linkId, String fileId);

    /**
     * 设置file业务关联关系
     * @param isDefOld 是否删除原有关联关系
     * @param linkId 业务关联ID
     * @param fileIds 文件ID
     */
    @Transactional(rollbackFor=Exception.class)
    void setFileLink(boolean isDefOld, String linkId, String... fileIds);

    /**
     * 根据业务关联ID查询文件列表
     * @param linkId 业务关联的ID
     * @return 文件列表
     */
    List<SysFileVO> getFilesByLinkId(String linkId);
}
