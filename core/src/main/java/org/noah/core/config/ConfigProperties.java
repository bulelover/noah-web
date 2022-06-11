package org.noah.core.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 系统字段定义配置
 * @author Noah
 */
@ConfigurationProperties(prefix = "config")
@Component
public class ConfigProperties {
    private String serverId;

    private String basedir;

    private String storageType;

    private Boolean requestLog;

    public String getServerId() {
        return StringUtils.isBlank(serverId)?"01" : serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getBasedir() {
        return StringUtils.isBlank(basedir)?"/home/app/files" : basedir;
    }

    public void setBasedir(String basedir) {
        this.basedir = basedir;
    }

    public String getStorageType() {
        return StringUtils.isBlank(storageType)?"normal" : storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public Boolean getRequestLog() {
        return requestLog == null || requestLog;
    }

    public void setRequestLog(Boolean requestLog) {
        this.requestLog = requestLog;
    }
}
