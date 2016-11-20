package org.monitoring.facade.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.monitoring.configuration.MonitoringConfiguration;
import org.monitoring.dto.ConfigurationRequest;
import org.monitoring.dto.MonitoringRequest;
import org.monitoring.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;


@RunWith(MockitoJUnitRunner.class)
public class DefaultMonitoringFacadeTest {

    @InjectMocks
    private DefaultMonitoringFacade facade;

    @Mock
    private MonitoringService service;

    @Mock
    private MonitoringConfiguration configuration;

    @Test
    public void shouldConsumeLogs() throws Exception {
        MonitoringRequest request = new MonitoringRequest();
        request.setMonitoringInterval(1L);
        facade.consumeLogs(request);

        Mockito.verify(service).consumeLogEntries(1L);
    }

    @Test
    public void shouldStart() throws Exception {
        facade.start();

        Mockito.verify(service).start();
    }

    @Test
    public void shouldStop() throws Exception {
        facade.stop();

        Mockito.verify(service).stop();
    }

}