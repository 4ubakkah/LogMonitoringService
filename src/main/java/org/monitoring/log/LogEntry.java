package org.monitoring.log;

import java.time.LocalDateTime;

public class LogEntry {

    private LogEntryType type;
    private LocalDateTime dateTime;
    private String message;

    public LogEntry(LogEntryType type, LocalDateTime dateTime, String message) {
        this.type = type;
        this.dateTime = dateTime;
        this.message = message;
    }

    public LogEntryType getType() {
        return type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }
}
