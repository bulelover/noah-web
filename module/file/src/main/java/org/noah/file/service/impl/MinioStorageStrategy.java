package org.noah.file.service.impl;

import cn.hutool.core.date.DateUtil;
import org.noah.core.utils.IDUtils;
import org.noah.file.service.IFileStorageService;
import org.noah.file.utils.FileUtils;
import org.noah.file.utils.MinioUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * <p>Minio存储服务</p>
 *
 * @author whli
 * @version 1.0.0
 * @since 2020/11/19 20:25
 */
public class MinioStorageStrategy implements IFileStorageService {

    @Value("${minio.default-bucket-name}")
    protected String bucketName;

    private byte[] query(String filePath) {
        String[] split = filePath.split("/");
        String bucketName = split[0];
        String realPath = String.join("/", Arrays.copyOfRange( split,1, split.length));
        return MinioUtils.download(bucketName, realPath);
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        String suffix = FileUtils.getExtension(file.getOriginalFilename());
        String[] date = DateUtil.format(new Date(), "yyyy-MM").split("-");
        String fileName = date[0] + '/' + date[1] + '/' + IDUtils.generateUUID() + "." + suffix;;
        return MinioUtils.upload(bucketName, file.getBytes(), fileName, file.getContentType());
    }

    @Override
    public void preview(String fileName, String filePath, String fileType, HttpServletResponse response) throws IOException {
        final byte[] bytes = query(filePath);
        FileUtils.preview(bytes, fileName, fileType, response);
    }

    @Override
    public void download(String fileName, String filePath, String fileType, HttpServletResponse response) throws IOException {
        final byte[] bytes = query(filePath);
        FileUtils.download(bytes, fileName, fileType, response);
    }
}
