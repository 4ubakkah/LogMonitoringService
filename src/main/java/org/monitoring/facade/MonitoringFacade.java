package org.monitoring.facade;

import org.monitoring.dto.ConfigurationRequest;
import org.monitoring.dto.MonitoringRequest;
import org.monitoring.dto.MonitoringResponse;

public interface MonitoringFacade {

    /**
     * Consumes logs stacked in service.
     * @param @MonitoringRequest request, intentionally holding no fields for purpose of extandability
     * @return MonitoringResponse with log entries within monitoring interval window configured on service start.
     */
    MonitoringResponse consumeLogs(MonitoringRequest request);

    /**
     * Configure monitoring service with provided configuration
     * @param @ConfigurationRequest request
     */
    void configure(ConfigurationRequest request);

    /**
     * Start monitoring thread.
     */
    void start();

    /**
     * Stop monitoring thread.
     */
    void stop();

}
