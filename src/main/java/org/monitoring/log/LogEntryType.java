package org.monitoring.log;


public enum LogEntryType {
    INFO("INFO"),
    WARNING("WARNING"),
    ERROR("ERROR");

    private String value;

    LogEntryType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static LogEntryType fromString(String text) {
        if (text != null) {
            for (LogEntryType b : LogEntryType.values()) {
                if (text.equalsIgnoreCase(b.value)) {
                    return b;
                }
            }
        }
        return null;
    }
}
