package com.healthcare.analytics.health.records.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HospitalMeaningfulUseResponse {
    private final String state;
    private final String stateCode;
    private final String year;
    private final Double percentage;
    private final String formattedPercentage;
}
