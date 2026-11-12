package com.healthcare.analytics.health.records.service;

import com.healthcare.analytics.health.records.dto.HospitalMeaningfulUseResponse;

import java.util.List;

public interface HospitalMeaningfulUseService {
    List<HospitalMeaningfulUseResponse> getHospitalsMeaningfulUseByYear(String year, boolean includeNational);
}
