package org.noah.file.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>Minio文件服务配置</p>
 *
 * @author wangheli
 * @version 1.0.0
 * @since 2021/7/3 08:44
 */
@ConfigurationProperties(prefix = "minio")
@Component
public class MinioProperties {

    /** 访问URL */
    private String url;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 桶名 */
    private String defaultBucketName;

    /**  地区 */
    private String region;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDefaultBucketName() {
        return StringUtils.isBlank(defaultBucketName)?"bucket":defaultBucketName;
    }

    public void setDefaultBucketName(String defaultBucketName) {
        this.defaultBucketName = defaultBucketName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
