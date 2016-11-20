package org.monitoring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Observable;

@Component
@ConfigurationProperties(prefix="monitoring", ignoreUnknownFields = false)
public class MonitoringConfiguration {

    private String filePath;
    private long threadSleepTimeout;

    public MonitoringConfiguration() {}

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;

    }

    public long getThreadSleepTimeout() {
        return threadSleepTimeout;
    }

    public void setThreadSleepTimeout(long threadSleepTimeout) {
        this.threadSleepTimeout = threadSleepTimeout;
    }
}
