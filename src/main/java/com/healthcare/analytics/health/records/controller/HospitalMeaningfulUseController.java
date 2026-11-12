package com.healthcare.analytics.health.records.controller;

import com.healthcare.analytics.health.records.constant.AppConstants;
import com.healthcare.analytics.health.records.constant.LogConstants;
import com.healthcare.analytics.health.records.dto.HospitalMeaningfulUseResponse;
import com.healthcare.analytics.health.records.service.HospitalMeaningfulUseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/meaningful-use")
@RequiredArgsConstructor
public class HospitalMeaningfulUseController {

    private final HospitalMeaningfulUseService service;

    @GetMapping("/hospitals")
    public ResponseEntity<List<HospitalMeaningfulUseResponse>> getHospitalMeaningfulUse(
            @RequestParam(defaultValue = AppConstants.DEFAULT_YEAR) String year,
            @RequestParam(defaultValue = "false") boolean includeNational) {

        log.info(LogConstants.CTRL_REQUEST_RECEIVED, year, includeNational);

        List<HospitalMeaningfulUseResponse> result = service.getHospitalsMeaningfulUseByYear(year, includeNational);

        log.info(LogConstants.CTRL_REQUEST_SUCCESS, result.size(), year);
        return ResponseEntity.ok(result);
    }
}
