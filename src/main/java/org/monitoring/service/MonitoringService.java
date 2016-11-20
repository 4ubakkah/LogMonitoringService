package org.monitoring.service;

import org.monitoring.configuration.MonitoringConfiguration;
import org.monitoring.log.LogEntry;

import java.util.List;

public interface MonitoringService {

    /**
     * Consumes logs stacked in service, non conforming records are eliminated
     * @return List<LogEntry> list of entries in log conforming to defined standard.
     */
    List<LogEntry> consumeLogEntries(long monitoringInterval);

    /**
     * Start daemon monitoring service with latest configuration.
     */
    void start();

    /**
     * Stop daemon monitoring service.
     */
    void stop();
}
