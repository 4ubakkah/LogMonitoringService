package org.monitoring.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.monitoring.configuration.MonitoringConfiguration;
import org.monitoring.facade.impl.DefaultMonitoringFacade;
import org.monitoring.log.LogEntry;
import org.monitoring.log.LogEntryType;
import org.monitoring.log.LogParser;
import org.monitoring.service.MonitoringService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
        Mockito.when(configuration.getMonitoringInterval()).thenReturn(1L);
        service.nonConsumedLogsEntries.add(logEntry);

        List<LogEntry> consumedEntries = service.consumeLogEntries();

        assertThat(consumedEntries).isNotEmpty();
        assertThat(consumedEntries).hasSize(1);
        assertThat(consumedEntries.stream().findFirst().get()).isEqualTo(logEntry);
    }

    @Test
    public void shouldNotConsumeLogEntry() throws Exception {
        Mockito.when(configuration.getMonitoringInterval()).thenReturn(1L);
        LogEntry logEntry = new LogEntry(LogEntryType.ERROR,
                LocalDateTime.of(LocalDate.now(), LocalTime.now().minusSeconds(2)),
                "Message");

        service.nonConsumedLogsEntries.add(logEntry);

        List<LogEntry> consumedEntries = service.consumeLogEntries();

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

    @Test
    public void shouldUpdate() throws Exception {
        service.update(configuration, new Object());

        Mockito.verify(service).stop();
        Mockito.verify(service).start();

        //TODO examine nonConsumedLogsEntries when field is externalized in service
        //assertThat(service.nonConsumedLogsEntries).isEmpty();
    }
}