package com.healthcare.analytics.health.records.service.impl;

import com.healthcare.analytics.health.records.constant.AppConstants;
import com.healthcare.analytics.health.records.constant.LogConstants;
import com.healthcare.analytics.health.records.dto.HospitalMeaningfulUseResponse;
import com.healthcare.analytics.health.records.dto.RawScorecardRecord;
import com.healthcare.analytics.health.records.handler.ScorecardHandler;
import com.healthcare.analytics.health.records.service.HospitalMeaningfulUseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalMeaningfulUseServiceImpl implements HospitalMeaningfulUseService {

    private final ScorecardHandler scorecardHandler;

    @Override
    public List<HospitalMeaningfulUseResponse> getHospitalsMeaningfulUseByYear(String year, boolean includeNational) {
        log.info(LogConstants.SERVICE_FILTER_SORT_START, year);

        List<RawScorecardRecord> rawRecords = scorecardHandler.fetchAllRecords();

        List<HospitalMeaningfulUseResponse> processedRecords = rawRecords.stream()
                .filter(r -> year.equalsIgnoreCase(r.getPeriod()))
                .filter(r -> r.getPctHospitalsMu() != null)
                // Exclude 'National' aggregate when strictly querying state records
                .filter(r -> includeNational ||
                        (!AppConstants.NATIONAL_REGION_CODE.equalsIgnoreCase(r.getRegionCode())
                                && !AppConstants.NATIONAL_REGION_NAME.equalsIgnoreCase(r.getRegion())))
                .sorted(Comparator.comparing(RawScorecardRecord::getPctHospitalsMu).reversed())
                .map(this::mapToResponse)
                .toList();

        log.info(LogConstants.SERVICE_FILTER_SORT_COMPLETED, processedRecords.size());
        return processedRecords;
    }

    private HospitalMeaningfulUseResponse mapToResponse(RawScorecardRecord record) {
        double percentageVal = record.getPctHospitalsMu() * 100.0;
        return HospitalMeaningfulUseResponse.builder()
                .state(record.getRegion())
                .stateCode(record.getRegionCode())
                .year(record.getPeriod())
                .percentage(record.getPctHospitalsMu())
                .formattedPercentage(String.format("%.2f%%", percentageVal))
                .build();
    }
}
