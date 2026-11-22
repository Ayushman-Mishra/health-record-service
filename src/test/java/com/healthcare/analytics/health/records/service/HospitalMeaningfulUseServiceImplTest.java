package com.healthcare.analytics.health.records.service.impl;

import com.healthcare.analytics.health.records.dto.HospitalMeaningfulUseResponse;
import com.healthcare.analytics.health.records.dto.RawScorecardRecord;
import com.healthcare.analytics.health.records.handler.ScorecardHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HospitalMeaningfulUseServiceImplTest {

    @Mock
    private ScorecardHandler scorecardHandler;

    @InjectMocks
    private HospitalMeaningfulUseServiceImpl service;

    @Test
    void shouldFilterByYearExcludeNationalAndSortDescending() {
        List<RawScorecardRecord> mockData = List.of(
                RawScorecardRecord.builder().region("Iowa").regionCode("IA").period("2014").pctHospitalsMu(0.99).build(),
                RawScorecardRecord.builder().region("Wyoming").regionCode("WY").period("2014").pctHospitalsMu(1.0).build(),
                RawScorecardRecord.builder().region("National").regionCode("US").period("2014").pctHospitalsMu(0.91).build(),
                RawScorecardRecord.builder().region("Texas").regionCode("TX").period("2013").pctHospitalsMu(0.95).build()
        );

        when(scorecardHandler.fetchAllRecords()).thenReturn(mockData);

        List<HospitalMeaningfulUseResponse> result = service.getHospitalsMeaningfulUseByYear("2014", false);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(HospitalMeaningfulUseResponse::getStateCode).containsExactly("WY", "IA");
        assertThat(result.get(0).getFormattedPercentage()).isEqualTo("100.00%");
    }

    @Test
    void shouldIncludeNationalWhenFlagIsTrue() {
        List<RawScorecardRecord> mockData = List.of(
                RawScorecardRecord.builder().region("Wyoming").regionCode("WY").period("2014").pctHospitalsMu(1.0).build(),
                RawScorecardRecord.builder().region("National").regionCode("US").period("2014").pctHospitalsMu(0.91).build()
        );

        when(scorecardHandler.fetchAllRecords()).thenReturn(mockData);

        List<HospitalMeaningfulUseResponse> result = service.getHospitalsMeaningfulUseByYear("2014", true);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(HospitalMeaningfulUseResponse::getStateCode).containsExactly("WY", "US");
    }
}