package org.monitoring.service.impl;

import org.monitoring.configuration.MonitoringConfiguration;
import org.monitoring.log.LogEntry;
import org.monitoring.log.LogParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;


public class MonitoringThread extends Thread{
    /**
     * Flag used to cancel file monitoring.
     */
    protected boolean monitoringRequired = true;

    protected final MonitoringConfiguration configuration;

    /**
     * List of LogEntries not consumed by client application.
     */
    protected List<LogEntry> nonConsumedLogsEntries;

    public MonitoringThread(MonitoringConfiguration configuration, List<LogEntry> nonConsumedLogsEntries) {
        this.nonConsumedLogsEntries = nonConsumedLogsEntries;
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
                    //System.out.println("No updates detected, going to sleep.");
                    Thread.sleep(configuration.getThreadSleepTimeout());
                } else {
                    //System.out.println("New log record detected: " + line);
                    LogEntry logEntry = LogParser.parseLogEntry(line);
                    if (logEntry != null) {
                        nonConsumedLogsEntries.add(logEntry);
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
