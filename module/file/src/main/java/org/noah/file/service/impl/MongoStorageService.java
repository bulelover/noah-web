package org.noah.file.service.impl;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.noah.core.utils.IDUtils;
import org.noah.file.service.IFileStorageService;
import org.noah.file.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>Mongo存储服务</p>
 *
 * @author whli745
 * @version 1.0.0
 * @since 2020/11/19 21:23
 */
public class MongoStorageService implements IFileStorageService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Override
    public String upload(MultipartFile file) throws IOException {
        String suffix = FileUtils.getExtension(file.getOriginalFilename());
        String fileName = IDUtils.generateUUID() + "." + suffix;
        ObjectId store = gridFsTemplate.store(file.getInputStream(), fileName, file.getContentType());
        return store.toString();
    }

    @Override
    public void preview(String fileName, String filePath, String fileType, HttpServletResponse response) throws IOException {
        byte[] buffer = convertToByte(filePath);
        if (buffer != null && buffer.length > 0) {
            FileUtils.preview(buffer, fileName, fileType, response);
        }
    }

    @Override
    public void download(String fileName, String filePath, String fileType, HttpServletResponse response) throws IOException {
        byte[] buffer = convertToByte(filePath);
        if (buffer != null && buffer.length > 0) {
            FileUtils.download(buffer, fileName, fileType, response);
        }
    }

    private byte[] convertToByte(String filePath) throws IOException {
        Query query = Query.query(Criteria.where("_id").is(filePath));
        // 查询单个文件
        GridFSFile gfsFile = gridFsTemplate.findOne(query);
        if (gfsFile == null) {
            return null;
        }
        GridFsResource resource = gridFsTemplate.getResource(gfsFile);
        try (InputStream inputStream = resource.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int num = inputStream.read(buffer);
            while (num != -1) {
                bos.write(buffer, 0, num);
                num = inputStream.read(buffer);
            }
            bos.flush();
            return bos.toByteArray();
        }
    }

}
