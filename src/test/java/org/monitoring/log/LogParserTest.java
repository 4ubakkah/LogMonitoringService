package org.monitoring.log;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

public class LogParserTest {

    @Test
    public void shouldParseLogEntry() throws Exception {
        String logEntryString = "2016-09-20 16:23:14,994 ERROR Some error message";

        LogEntry logEntry = LogParser.parseLogEntry(logEntryString);

        LocalTime expectedTime = LocalTime.of(16, 23, 14, 994000000);

        assertThat(logEntry.getDateTime())
            .isEqualToIgnoringGivenFields(LocalDateTime.of(LocalDate.of(2016, 9, 20), expectedTime), "nano");
        assertThat(logEntry.getMessage()).isEqualTo("Some error message");
        assertThat(logEntry.getType()).isEqualTo(LogEntryType.ERROR);
    }

    @Test
    public void shouldNotParseLogEntry_when_wrongLogFormatIsProvided() throws Exception {
        String logEntryString = "2016-09-20T16:23:14 ERROR Some error message";

        LogEntry logEntry = LogParser.parseLogEntry(logEntryString);

        assertThat(logEntry).isNull();
    }

}