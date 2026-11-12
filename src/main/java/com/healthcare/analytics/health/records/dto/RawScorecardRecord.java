package com.healthcare.analytics.health.records.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RawScorecardRecord {
    private final String region;
    private final String regionCode;
    private final String period;
    private final Double pctHospitalsMu;
}
