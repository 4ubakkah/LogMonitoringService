package org.monitoring.log;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

    protected static final String STANDARD_TIME_PRESENTATION = "yyyy-MM-dd HH:mm:ss,SSS";
    protected static final String TIMESTAMP_PATTERN = "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\,\\d{3})";
    protected static final String ENTRY_TYPE_PATTERN = "(INFO|WARNING|ERROR)";
    protected static final String MESSAGE_PATTERN = "(.*)";
    protected static final String DELIMITER_PATTERN = " ";
    protected static final String LOG_ENTRY_PATTERN = TIMESTAMP_PATTERN + DELIMITER_PATTERN + ENTRY_TYPE_PATTERN
            + DELIMITER_PATTERN + MESSAGE_PATTERN;

    /**
     * Pattern defining canonical entry in log file.
     */
    private static final Pattern pattern = Pattern.compile(LOG_ENTRY_PATTERN);

    /**
     * Parse string representing log entry.
     * @param @String retrieved from log file.
     * @return LogEntry or null if parsed record doesn't conform to defined format
     */
    public static LogEntry parseLogEntry(String logEntryString) {
        LogEntry logEntry = null;

        Matcher matcher = pattern.matcher(logEntryString);

        if (matcher.matches()) {
            logEntry = new LogEntry(LogEntryType.fromString(matcher.group(2)),
                    LocalDateTime.parse(matcher.group(1), DateTimeFormatter.ofPattern(STANDARD_TIME_PRESENTATION)),
                    matcher.group(3));
        }

        return logEntry;
    }
}
