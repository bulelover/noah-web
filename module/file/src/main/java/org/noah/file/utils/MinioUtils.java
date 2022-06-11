package org.noah.file.utils;

import io.minio.*;
import org.apache.commons.io.IOUtils;
import org.noah.core.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * <p>Minio存储文件工具类</p>
 *
 * @author wangheli
 * @version 1.0.0
 * @since 2021/7/3 09:25
 */
@Component
public final class MinioUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioUtils.class);
    private static MinioUtils utils;
    private MinioUtils() {}

    @Autowired
    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        utils = this;
        utils.minioClient = this.minioClient;
    }

    /***
     * <p>验证存储桶是否存在</p>
     *
     * @author whli
     * @since 2021/7/3 09:29
     * @param bucketName 存储桶名
     * @return
     */
    public static boolean bucketExists(String bucketName) {
        try {
            final BucketExistsArgs build = BucketExistsArgs.builder().bucket(bucketName).build();
            return utils.minioClient.bucketExists(build);
        } catch (Exception e) {
            throw new BusinessException("Minio服务验证存储桶失败");
        }
    }

    /***
     * <p>创建存储桶</p>
     *
     * @author whli
     * @since 2021/7/3 09:35
     * @param bucketName 存储桶名
     * @return
     */
    public static void createBucketIfNotExists(String bucketName) {
        boolean flag = bucketExists(bucketName);
        try {
            if (!flag) {
                final MakeBucketArgs bucketArgs = MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build();
                utils.minioClient.makeBucket(bucketArgs);
            }
        } catch (Exception e) {
            throw new BusinessException("Minio服务创建存储桶失败");
        }
    }

    /***
     * <p>上传文件</p>
     *
     * @author whli
     * @since 2021/7/3 14:04
     * @param bucketName  存储桶名
     * @param fileName    文件名
     * @param source      上传文件
     * @param contentType 上传文件类型
     * @return String 存储文件名
     */
    public static String upload(String bucketName, byte[] source, String fileName, String contentType) {
        createBucketIfNotExists(bucketName);
        try (InputStream stream = new ByteArrayInputStream(source)) {
            final PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .contentType(contentType)
                    .stream(stream, stream.available(), -1L)
                    .build();
            utils.minioClient.putObject(objectArgs);
            return bucketName.concat("/").concat(fileName);

        } catch (Exception e) {
            throw new BusinessException("Minio上传文件错误,"+e.getMessage());
        }
    }

    /***
     * <p>下载文件</p>
     *
     * @author whli
     * @since 2021/7/3 14:28
     * @param bucketName 存储桶名
     * @param fileName   存储文件名
     * @return byte[] 字节数组
     */
    public static byte[] download(String bucketName, String fileName) {
        if (!validObject(bucketName, fileName)) {
            throw new BusinessException("Minio下载文件错误,文件可能不存在");
        }

        final GetObjectArgs objectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build();
        try (GetObjectResponse response = utils.minioClient.getObject(objectArgs)) {
            return IOUtils.toByteArray(response);

        } catch (Exception e) {
            throw new BusinessException("Minio下载文件错误,"+e.getMessage());
        }
    }

    /***
     * <p>验证文件是否存在</p>
     *
     * @author whli
     * @since 2021/7/3 14:33
     * @param bucketName 存储桶
     * @param objectName 存储文件名
     * @return
     */
    public static boolean validObject(String bucketName, String objectName) {
        try {
            final StatObjectArgs objectArgs = StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build();
            StatObjectResponse response = utils.minioClient.statObject(objectArgs);
            return response != null;
        } catch (Exception e) {
            LOGGER.error("Minio服务验证文件失败", e);
        }
        return false;
    }

}
