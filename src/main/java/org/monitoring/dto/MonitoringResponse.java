package org.monitoring.dto;

import org.monitoring.log.LogEntry;
import org.monitoring.log.LogEntryType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonitoringResponse {

    private List<LogEntry> logEntries;

    private HashMap<LogEntryType, Long> statistics;

    public MonitoringResponse(HashMap<LogEntryType, Long> statistics) {
        this.statistics = statistics;
        logEntries = new ArrayList<>();
    }

    public List<LogEntry> getLogEntries() {
        return logEntries;
    }

    public void setLogEntries(List<LogEntry> logEntries) {
        this.logEntries = logEntries;
    }

    public HashMap<LogEntryType, Long> getStatistics() {
        return statistics;
    }

    public void setStatistics(HashMap<LogEntryType, Long> statistics) {
        this.statistics = statistics;
    }
}
