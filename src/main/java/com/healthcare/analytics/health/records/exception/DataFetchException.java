package com.healthcare.analytics.health.records.exception;

public class DataFetchException extends RuntimeException {
    public DataFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
