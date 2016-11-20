package org.monitoring.service.impl;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.monitoring.configuration.MonitoringConfiguration;
import org.monitoring.log.LogEntry;
import org.monitoring.log.LogEntryType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class AdvancedMonitoringServiceTest {

    @InjectMocks
    @Spy
    private AdvancedMonitoringService service;

    @Mock
    private MonitoringConfiguration configuration;
    @Mock
    private LinkedList<LogEntry> nonConsumedLogsEntries;

    @Test
    public void shouldConsumeLogEntry() throws Exception {
        LogEntry logEntry =  new LogEntry(LogEntryType.ERROR,
                LocalDateTime.of(LocalDate.now(), LocalTime.now()),
                "Message");
        service.logEntries.add(logEntry);

        List<LogEntry> consumedEntries = service.consumeLogEntries(1L);

        assertThat(consumedEntries).isNotEmpty();
        assertThat(consumedEntries.size()).isGreaterThan(0);
        assertThat(consumedEntries.stream().findFirst().get()).isEqualTo(logEntry);
    }

    @Test
    public void shouldNotConsumeLogEntry() throws Exception {
        LogEntry logEntry = new LogEntry(LogEntryType.ERROR,
                LocalDateTime.of(LocalDate.now(), LocalTime.now().minusSeconds(2)),
                "Message");

        service.logEntries.add(logEntry);

        List<LogEntry> consumedEntries = service.consumeLogEntries(1L);

        assertThat(consumedEntries).isEmpty();
    }

    @Test
    public void shouldStart() throws Exception {
        service.start();

        assertThat(service.monitoringThread).isNotNull();
        assertThat(service.monitoringThread.isDaemon()).isTrue();
    }

    @Test
    public void shouldStop() throws Exception {
        service.monitoringThread = new MonitoringThread(configuration, nonConsumedLogsEntries);

        service.start();
        service.stop();

        service.monitoringThread.join();

        assertThat(service.monitoringThread).isNotNull();
        assertThat(service.monitoringThread.monitoringRequired).isFalse();
        //TODO check monitoringThread state, expect thread to be terminated
    }
}