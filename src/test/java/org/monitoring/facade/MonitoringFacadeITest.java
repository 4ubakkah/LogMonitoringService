package org.monitoring.facade;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.monitoring.dto.ConfigurationRequest;
import org.monitoring.dto.MonitoringRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MonitoringFacadeITest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private MappingJackson2HttpMessageConverter jsonToHttpMessageConverter;

    private MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        jsonToHttpMessageConverter = (MappingJackson2HttpMessageConverter)Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();
        jsonToHttpMessageConverter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldSuccessfullyInvokeConsumeLogs() throws Exception {
        MonitoringRequest monitoringRequest = new MonitoringRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/monitoring/consume")
                .content(this.convertToJson(monitoringRequest))
                .contentType(jsonContentType))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void configure() throws Exception {
        ConfigurationRequest configurationRequest = new ConfigurationRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/monitoring/configure")
                .content(this.convertToJson(configurationRequest))
                .contentType(jsonContentType))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void start() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/monitoring/start")
                .contentType(jsonContentType))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void stop() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/monitoring/start")
                .contentType(jsonContentType))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/rest/monitoring/stop")
                .contentType(jsonContentType))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    protected String convertToJson(Object input) throws IOException {
        MockHttpOutputMessage httpMessage = new MockHttpOutputMessage();
        jsonToHttpMessageConverter.write(input, MediaType.APPLICATION_JSON, httpMessage);
        return httpMessage.getBodyAsString();
    }

}