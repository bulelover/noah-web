package org.noah.file.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>文件上传下载服务</p>
 *
 * @author whli745
 * @version 1.0.0
 * @since 2020/11/19 19:24
 */
@Service
public interface IFileStorageService {



    /**
     * <p>文件上传服务</p>
     *
     * @author wangheli
     * @since 2020/11/19 19:26
     * @param file 上传文件
     * @return
     */
    String upload(MultipartFile file) throws IOException;

    /**
     * <p>文件预览</p>
     *
     * @author wangheli
     * @since 2020/11/19 19:28
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param fileType 文件类型
     * @param response 请求响应
     * @return
     */
    void preview(String fileName, String filePath, String fileType, HttpServletResponse response) throws IOException;

    /**
     * <p>文件下载服务</p>
     *
     * @author wangheli
     * @since 2020/11/19 19:26
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param fileType 文件类型
     * @param response 请求响应
     * @return
     */
    void download(String fileName, String filePath, String fileType, HttpServletResponse response) throws IOException;

}
