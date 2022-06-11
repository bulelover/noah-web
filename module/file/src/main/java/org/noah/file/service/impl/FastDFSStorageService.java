package org.noah.file.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.noah.core.utils.IDUtils;
import org.noah.file.service.IFileStorageService;
import org.noah.file.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>FastDFS存储服务</p>
 *
 * @author whli745
 * @version 1.0.0
 * @since 2020/11/19 20:25
 */
public class FastDFSStorageService implements IFileStorageService {

    @Autowired
    private FastFileStorageClient storageClient;

    @Override
    public String upload(MultipartFile file) throws IOException {
        String suffix = FileUtils.getExtension(file.getOriginalFilename());
        String fileName = IDUtils.generateUUID() + "." + suffix;
        StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
                fileName,null);
        return storePath.getFullPath();
    }

    @Override
    public void preview(String fileName, String filePath, String fileType, HttpServletResponse response) throws IOException {
        StorePath storePath = StorePath.parseFromUrl(filePath);
        byte[] bytes = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
        FileUtils.preview(bytes, fileName, fileType, response);
    }

    @Override
    public void download(String fileName, String filePath, String fileType, HttpServletResponse response) throws IOException {
        StorePath storePath = StorePath.parseFromUrl(filePath);
        byte[] bytes = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
        FileUtils.download(bytes, fileName, fileType, response);
    }

}
