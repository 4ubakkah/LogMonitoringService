package org.monitoring.facade.impl;

import org.monitoring.configuration.MonitoringConfiguration;
import org.monitoring.dto.ConfigurationRequest;
import org.monitoring.dto.MonitoringRequest;
import org.monitoring.dto.MonitoringResponse;
import org.monitoring.facade.MonitoringFacade;
import org.monitoring.log.LogEntry;
import org.monitoring.log.LogEntryType;
import org.monitoring.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/rest/monitoring")
public class DefaultMonitoringFacade implements MonitoringFacade {

    @Autowired
    private MonitoringService service;

    @Autowired
    private MonitoringConfiguration configuration;

    @RequestMapping(value="/consume", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Override
    public MonitoringResponse consumeLogs(@RequestBody MonitoringRequest request) {
        return composeMonitoringResponse(service.consumeLogEntries());
    }

    @RequestMapping(value="/configure", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Override
    public void configure(@RequestBody ConfigurationRequest request) {
        configuration.updateConfiguration(request.getFilePath(), request.getMonitoringInterval());
    }

    @RequestMapping(value="/start", method= RequestMethod.POST)
    @ResponseBody
    @Override
    public void start() {
        service.start();
    }

    @RequestMapping(value="/stop", method= RequestMethod.POST)
    @ResponseBody
    @Override
    public void stop() {
        service.stop();
    }

    private MonitoringResponse composeMonitoringResponse(List<LogEntry> logEntries) {
        HashMap<LogEntryType,Long> counter = new HashMap<>(LogEntryType.values().length,1);
        for(LogEntry logEntry : logEntries) {
            Long count = counter.get(logEntry.getType());
            if(count == null) counter.put(logEntry.getType(), 1L);
            else counter.put(logEntry.getType(), count + 1);
        }

        MonitoringResponse response = new MonitoringResponse(counter);

        return response;
    }
}
