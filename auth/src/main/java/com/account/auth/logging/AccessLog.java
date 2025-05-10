package com.account.auth.logging;

import java.time.LocalDateTime;
import java.util.Map;

public class AccessLog {
    private LocalDateTime timestamp;
    private String requestApi;
    private Map<String, String> requestHeaders;
    private String requestBody;
    private Map<String, String> responseHeaders;
    private String responseBody;
    private long responseTime;
    private int responseHttpCode;

    public AccessLog() {
        this.timestamp = LocalDateTime.now();
    }

    public void setRequestApi(String requestApi) {
        this.requestApi = requestApi;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public void setResponseHttpCode(int responseHttpCode) {
        this.responseHttpCode = responseHttpCode;
    }

    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s|%s|%s|%dms|%d",
                timestamp,
                requestApi,
                requestHeaders,
                requestBody,
                responseHeaders,
                responseBody,
                responseTime,
                responseHttpCode);
    }
} 