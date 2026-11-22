package com.healthcare.analytics.health.records.controller;

import com.healthcare.analytics.health.records.dto.HospitalMeaningfulUseResponse;
import com.healthcare.analytics.health.records.exception.DataFetchException;
import com.healthcare.analytics.health.records.exception.GlobalExceptionHandler;
import com.healthcare.analytics.health.records.service.HospitalMeaningfulUseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HospitalMeaningfulUseController.class)
@Import(GlobalExceptionHandler.class)
class HospitalMeaningfulUseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HospitalMeaningfulUseService service;

    @Test
    void shouldReturnHospitalRecordsForDefaultYear() throws Exception {
        List<HospitalMeaningfulUseResponse> response = List.of(
                HospitalMeaningfulUseResponse.builder()
                        .state("Wyoming")
                        .stateCode("WY")
                        .year("2014")
                        .percentage(1.0)
                        .formattedPercentage("100.00%")
                        .build()
        );

        when(service.getHospitalsMeaningfulUseByYear("2014", false)).thenReturn(response);

        mockMvc.perform(get("/api/v1/meaningful-use/hospitals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].stateCode").value("WY"))
                .andExpect(jsonPath("$[0].formattedPercentage").value("100.00%"));
    }

    @Test
    void shouldReturnBadGatewayWhenDataFetchFails() throws Exception {
        when(service.getHospitalsMeaningfulUseByYear("2014", false))
                .thenThrow(new DataFetchException("External API unavailable", null));

        mockMvc.perform(get("/api/v1/meaningful-use/hospitals"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.status").value(502))
                .andExpect(jsonPath("$.message").value("External API unavailable"));
    }
}