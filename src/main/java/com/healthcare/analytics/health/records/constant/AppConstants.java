package com.healthcare.analytics.health.records.constant;

public class AppConstants {

    public static final String DATASET_URL =
            "https://healthit.gov/wp-content/uploads/data/datasets/Meaningful-Use-Acceleration-Scorecard.csv";

    public static final String DEFAULT_YEAR = "2014";
    public static final String NATIONAL_REGION_CODE = "US";
    public static final String NATIONAL_REGION_NAME = "National";

    // csv headers matching the source dataset
    public static final String CSV_HEADER_REGION = "region";
    public static final String CSV_HEADER_REGION_CODE = "region_code";
    public static final String CSV_HEADER_PERIOD = "period";
    public static final String CSV_HEADER_PCT_HOSPITALS_MU = "pct_hospitals_mu";
}
