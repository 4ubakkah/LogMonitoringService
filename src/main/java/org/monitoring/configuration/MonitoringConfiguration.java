package org.monitoring.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Observable;

@Component
@ConfigurationProperties(prefix="monitoring", ignoreUnknownFields = false)
public class MonitoringConfiguration extends Observable {

    private String filePath;
    private long monitoringInterval;
    private long threadSleepTimeout;

    public MonitoringConfiguration() {}

    public void updateConfiguration(String filePath, long monitoringInterval) {
        this.filePath = filePath;
        this.monitoringInterval = monitoringInterval;
        setChanged();
        notifyObservers();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;

    }

    public long getMonitoringInterval() {
        return monitoringInterval;
    }

    public void setMonitoringInterval(long monitoringInterval) {
        this.monitoringInterval = monitoringInterval;
    }

    public long getThreadSleepTimeout() {
        return threadSleepTimeout;
    }

    public void setThreadSleepTimeout(long threadSleepTimeout) {
        this.threadSleepTimeout = threadSleepTimeout;
    }
}
