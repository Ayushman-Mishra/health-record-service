package com.healthcare.analytics.health.records.handler;

import com.healthcare.analytics.health.records.constant.AppConstants;
import com.healthcare.analytics.health.records.dto.RawScorecardRecord;
import com.healthcare.analytics.health.records.exception.DataFetchException;
import com.healthcare.analytics.health.records.handler.impl.ScorecardHandlerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ExchangeFunction;
import org.springframework.web.client.RestClientException;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScorecardHandlerImplTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestClient restClient;

    private ScorecardHandlerImpl handler;

    @BeforeEach
    void setUp() {
        handler = new ScorecardHandlerImpl();
        ReflectionTestUtils.setField(handler, "restClient", restClient);
    }

    @Test
    void shouldParseCsvRecordsSuccessfully() throws Exception {
        String csv = """
                region,region_code,period,pct_hospitals_mu
                Wyoming,WY,2014,1.0
                Iowa,IA,2014,0.99
                """;

        ConvertibleClientHttpResponse response = mock(ConvertibleClientHttpResponse.class);
        when(response.getStatusCode()).thenReturn(HttpStatus.OK);
        when(response.getBody()).thenReturn(new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8)));

        when(restClient.get().uri(AppConstants.DATASET_URL).exchange(any())).thenAnswer(inv -> {
            ExchangeFunction<List<RawScorecardRecord>> fn = inv.getArgument(0);
            return fn.exchange(mock(HttpRequest.class), response);
        });

        List<RawScorecardRecord> records = handler.fetchAllRecords();

        assertThat(records).hasSize(2);
        assertThat(records.get(0).getRegionCode()).isEqualTo("WY");
        assertThat(records.get(0).getPctHospitalsMu()).isEqualTo(1.0);
    }

    @Test
    void shouldThrowDataFetchExceptionWhenNetworkFails() {
        when(restClient.get().uri(AppConstants.DATASET_URL).exchange(any()))
                .thenThrow(new RestClientException("Connection timeout"));

        assertThatThrownBy(() -> handler.fetchAllRecords())
                .isInstanceOf(DataFetchException.class);
    }
}