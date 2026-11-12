package com.healthcare.analytics.health.records.handler;

import com.healthcare.analytics.health.records.dto.RawScorecardRecord;

import java.util.List;

public interface ScorecardHandler {
    List<RawScorecardRecord> fetchAllRecords();
}
