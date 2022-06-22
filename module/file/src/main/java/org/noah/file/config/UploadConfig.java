package org.noah.file.config;

import io.minio.MinioClient;
import org.noah.file.service.impl.FastDFSStorageService;
import org.noah.file.service.impl.MinioStorageStrategy;
import org.noah.file.service.impl.MongoStorageService;
import org.noah.file.service.impl.NormalStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p></p>
 *
 * @author wangheli
 * @version 1.0.0
 * @since 2020/11/19 11:16
 */
@Configuration
public class UploadConfig {

    private final MinioProperties minioProperty;

    public UploadConfig(MinioProperties minioProperty) {
        this.minioProperty = minioProperty;
    }

    @ConditionalOnProperty(name = "config.storage-type", havingValue = "normal")
    @Bean
    public NormalStorageService normalStorageManager() {
        return new NormalStorageService();
    }

    @ConditionalOnProperty(name = "config.storage-type", havingValue = "fast_dfs")
    @Bean
    public FastDFSStorageService fastStorageService() {
        return new FastDFSStorageService();
    }

    @ConditionalOnProperty(name = "config.storage-type", havingValue = "mongo_db")
    @Bean
    public MongoStorageService mongoStorageService() {
        return new MongoStorageService();
    }

    @Bean
    public MinioClient minioStorageClient() {
        return MinioClient.builder()
                .endpoint(minioProperty.getUrl())
                .credentials(minioProperty.getUsername(), minioProperty.getPassword())
                .region(minioProperty.getRegion())
                .build();
    }

    @ConditionalOnProperty(name = "config.storage-type", havingValue = "minio")
    @Bean
    public MinioStorageStrategy minioStorageStrategy() {
        return new MinioStorageStrategy();
    }

}

