package org.monitoring.service.impl;

import org.monitoring.configuration.MonitoringConfiguration;
import org.monitoring.log.LogEntry;
import org.monitoring.log.LogParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Deque;


public class MonitoringThread extends Thread{
    /**
     * Flag used to cancel file monitoring.
     */
    protected boolean monitoringRequired = true;

    protected final MonitoringConfiguration configuration;

    /**
     * List of LogEntries not consumed by client application.
     */
    protected Deque<LogEntry> logEntries;

    public MonitoringThread(MonitoringConfiguration configuration, Deque<LogEntry> logEntries) {
        this.logEntries = logEntries;
        this.configuration = configuration;
    }

    /**
     * Reads updates from file with configured interval, procedure is cancel by external request only
     */
    @Override
    public void run() {
        File file = new File(configuration.getFilePath());
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (monitoringRequired) {

                String line = br.readLine();
                if (line == null) {
                    Thread.sleep(configuration.getThreadSleepTimeout());
                } else {
                    LogEntry logEntry = LogParser.parseLogEntry(line);
                    if (logEntry != null) {
                        logEntries.add(logEntry);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Provided file doesn't exist: " + configuration.getFilePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setMonitoringRequired(boolean monitoringRequired) {
        this.monitoringRequired = monitoringRequired;
    }
}
