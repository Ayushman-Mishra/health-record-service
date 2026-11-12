package com.healthcare.analytics.health.records.constant;

public class LogConstants {

    public static final String CTRL_REQUEST_RECEIVED =
            "Received request to fetch hospital meaningful use data for year: {}, includeNational: {}";
    public static final String CTRL_REQUEST_SUCCESS =
            "Successfully returned {} hospital records for year: {}";


    public static final String SERVICE_FILTER_SORT_START =
            "Filtering records for year: {} and sorting by hospital MU percentage descending";
    public static final String SERVICE_FILTER_SORT_COMPLETED =
            "Completed processing. Total valid state records: {}";


    public static final String DAO_FETCHING_DATASET =
            "Connecting to external endpoint to stream CSV dataset: {}";
    public static final String DAO_FETCH_SUCCESS =
            "Successfully downloaded and parsed {} raw records from external dataset";
    public static final String DAO_FETCH_ERROR =
            "Failed to fetch CSV dataset from endpoint: {}";
    public static final String DAO_PARSE_ERROR =
            "Error encountered while parsing CSV stream";


    public static final String EX_GLOBAL_ERROR =
            "Unhandled system exception occurred: {}";
    public static final String EX_BUSINESS_ERROR =
            "Business processing error occurred: {}";
}
