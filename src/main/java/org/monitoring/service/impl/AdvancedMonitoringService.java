package org.monitoring.service.impl;


import org.monitoring.configuration.MonitoringConfiguration;
import org.monitoring.log.LogEntry;
import org.monitoring.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class AdvancedMonitoringService implements MonitoringService{

    /**
     * Daemon thread facilitating file monitoring.
     */
    protected MonitoringThread monitoringThread;

    /**
     * List of LogEntries not consumed by client application injected into monitoring thread.
     */
    protected Deque<LogEntry> logEntries;

    protected final MonitoringConfiguration configuration;

    @Autowired
    public AdvancedMonitoringService(MonitoringConfiguration configuration) {
        logEntries = new ConcurrentLinkedDeque<>();
        this.configuration = configuration;
    }

    @Override
    public List<LogEntry> consumeLogEntries(long monitoringInterval) {
        List<LogEntry> entriesToConsume = new LinkedList<>();

        Iterator<LogEntry> iterator = logEntries.iterator();

        while(iterator.hasNext()) {
            LogEntry logEntry = iterator.next();
            Duration durationBetweenNowAndLogTime = Duration.between(logEntry.getDateTime(), LocalDateTime.now());
            if(Math.abs(durationBetweenNowAndLogTime.getSeconds()) <= monitoringInterval) {
                entriesToConsume.add(logEntry);
            } else {
                break;
            }
        }

        return entriesToConsume;
    }

    @Override
    public void start() {
        if(monitoringThread == null || monitoringThread.getState().equals(Thread.State.TERMINATED)) {
            monitoringThread = new MonitoringThread(configuration, logEntries);
            monitoringThread.setDaemon(true);
            monitoringThread.start();
        }
    }

    @Override
    public void stop() {
        if(monitoringThread == null) {
            return;
        }

        monitoringThread.setMonitoringRequired(false);

        try {
            monitoringThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
