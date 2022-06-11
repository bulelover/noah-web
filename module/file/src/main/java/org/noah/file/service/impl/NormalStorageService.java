package org.noah.file.service.impl;

import cn.hutool.core.date.DateUtil;
import org.noah.core.exception.BusinessException;
import org.noah.core.utils.IDUtils;
import org.noah.file.service.IFileStorageService;
import org.noah.file.utils.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * <p>本体文件存储方式</p>
 *
 * @author whli745
 * @version 1.0.0
 * @since 2020/11/19 19:28
 */
public class NormalStorageService implements IFileStorageService {

    @Override
    public String upload(MultipartFile file) throws IOException {
        return uploadFile(file);
    }

    @Override
    public void preview(String fileName, String filePath, String fileType, HttpServletResponse response) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            FileUtils.preview(buffer, fileName, fileType, response);
        }
    }

    @Override
    public void download(String fileName, String filePath, String fileType, HttpServletResponse response) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            InputStream fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            FileUtils.download(buffer, fileName, fileType, response);
        }
    }

    private String uploadFile(MultipartFile file) throws IOException {
        String suffix = FileUtils.getExtension(file.getOriginalFilename());
        String fileName = IDUtils.generateUUID() + "." + suffix;
        String[] date = DateUtil.format(new Date(), "yyyy-MM").split("-");
        String filePath =  File.separator + date[0] + File.separator + date[1];
        File directory = new File( FileUtils.createPath() + filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String dirPath = directory.getAbsolutePath() + File.separator + fileName;
        File tempFile = new File(dirPath);
        file.transferTo(tempFile);
        return dirPath;
    }

}
