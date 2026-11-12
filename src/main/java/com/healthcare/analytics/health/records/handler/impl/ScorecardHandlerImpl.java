package com.healthcare.analytics.health.records.handler.impl;

import com.healthcare.analytics.health.records.constant.AppConstants;
import com.healthcare.analytics.health.records.constant.LogConstants;
import com.healthcare.analytics.health.records.dto.RawScorecardRecord;
import com.healthcare.analytics.health.records.exception.DataFetchException;
import com.healthcare.analytics.health.records.exception.DataProcessingException;
import com.healthcare.analytics.health.records.handler.ScorecardHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class ScorecardHandlerImpl implements ScorecardHandler {

    private final RestClient restClient;

    public ScorecardHandlerImpl() {
        this.restClient = RestClient.builder().build();
    }

    @Override
    public List<RawScorecardRecord> fetchAllRecords() {
        log.info(LogConstants.DAO_FETCHING_DATASET, AppConstants.DATASET_URL);

        try {
            return restClient.get()
                    .uri(AppConstants.DATASET_URL)
                    .exchange((request, response) -> {
                        if (!response.getStatusCode().is2xxSuccessful()) {
                            throw new DataFetchException(
                                    "Upstream server returned HTTP " + response.getStatusCode(), null);
                        }

                        try (Reader reader = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
                             CSVParser csvParser = CSVFormat.DEFAULT.builder()
                                     .setHeader()
                                     .setSkipHeaderRecord(true)
                                     .setIgnoreHeaderCase(true)
                                     .setTrim(true)
                                     .build()
                                     .parse(reader)) {

                            List<RawScorecardRecord> records = new ArrayList<>();
                            for (CSVRecord record : csvParser) {
                                String pctRaw = record.get(AppConstants.CSV_HEADER_PCT_HOSPITALS_MU);
                                Double pctValue = (pctRaw != null && !pctRaw.isBlank())
                                        ? Double.parseDouble(pctRaw)
                                        : null;

                                records.add(RawScorecardRecord.builder()
                                        .region(record.get(AppConstants.CSV_HEADER_REGION))
                                        .regionCode(record.get(AppConstants.CSV_HEADER_REGION_CODE))
                                        .period(record.get(AppConstants.CSV_HEADER_PERIOD))
                                        .pctHospitalsMu(pctValue)
                                        .build());
                            }
                            log.info(LogConstants.DAO_FETCH_SUCCESS, records.size());
                            return records;
                        } catch (Exception ex) {
                            log.error(LogConstants.DAO_PARSE_ERROR, ex);
                            throw new DataProcessingException("Failed to parse CSV dataset", ex);
                        }
                    });
        } catch (DataProcessingException | DataFetchException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error(LogConstants.DAO_FETCH_ERROR, AppConstants.DATASET_URL, ex);
            throw new DataFetchException("Failed to stream dataset from external source", ex);
        }
    }
}
