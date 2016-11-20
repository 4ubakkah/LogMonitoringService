package org.monitoring.service.impl;


import org.monitoring.configuration.MonitoringConfiguration;
import org.monitoring.log.LogEntry;
import org.monitoring.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AdvancedMonitoringService implements MonitoringService, Observer {

    /**
     * Daemon thread facilitating file monitoring.
     */
    protected MonitoringThread monitoringThread;

    /**
     * List of LogEntries not consumed by client application injected into monitoring thread.
     */
    protected volatile LinkedList<LogEntry> nonConsumedLogsEntries;

    protected final MonitoringConfiguration configuration;

    @Autowired
    public AdvancedMonitoringService(MonitoringConfiguration configuration) {
        nonConsumedLogsEntries = new LinkedList<>();
        this.configuration = configuration;
        configuration.addObserver(this);
    }

    @Override
    public synchronized List<LogEntry> consumeLogEntries() {
        List<LogEntry> entriesToConsume = new ArrayList<>(nonConsumedLogsEntries.size());

        while (!nonConsumedLogsEntries.isEmpty()) {
            LogEntry logEntry = nonConsumedLogsEntries.remove();
            Duration durationBetweenNowAndLogTime = Duration.between(logEntry.getDateTime(), LocalDateTime.now());

            if(Math.abs(durationBetweenNowAndLogTime.getSeconds()) <= configuration.getMonitoringInterval()) {
                entriesToConsume.add(logEntry);
            }
        }

        return entriesToConsume;
    }

    @Override
    public synchronized void start() {
        if(monitoringThread == null || monitoringThread.getState().equals(Thread.State.TERMINATED)) {
            monitoringThread = new MonitoringThread(configuration, nonConsumedLogsEntries);
            monitoringThread.setDaemon(true);
            monitoringThread.start();
        }
    }

    @Override
    public synchronized void stop() {
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

    /**
     * Called on monitoring configuration update to restart monitoring procedure with updated configuration.
     */
    @Override
    public synchronized void update(Observable o, Object arg) {
        if (o instanceof MonitoringConfiguration) {
            System.out.println("Logging file path has changed, restarting service.");

            stop();

            try {
                if (monitoringThread != null) {
                    monitoringThread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            nonConsumedLogsEntries.clear();

            start();
        }
    }
}
