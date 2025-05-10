package com.account.auth.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class AccessLogInterceptor implements HandlerInterceptor {
    private final AccessLogger accessLogger;
    private final ThreadLocal<AccessLog> accessLogThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();
    
    private static final Set<String> EXCLUDED_HEADERS = new HashSet<>(Arrays.asList(
        "user-agent",
        "accept",
        "accept-encoding",
        "accept-language",
        "connection",
        "cache-control",
        "sec-ch-ua",
        "sec-ch-ua-mobile",
        "sec-ch-ua-platform",
        "sec-fetch-dest",
        "sec-fetch-mode",
        "sec-fetch-site",
        "upgrade-insecure-requests"
    ));

    public AccessLogInterceptor(AccessLogger accessLogger) {
        this.accessLogger = accessLogger;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        startTimeThreadLocal.set(System.currentTimeMillis());
        AccessLog accessLog = new AccessLog();
        accessLog.setRequestApi(request.getMethod() + " " + request.getRequestURI());
        accessLog.setRequestHeaders(getRequestHeaders(request));
        accessLog.setRequestBody(getRequestBody(request));
        accessLogThreadLocal.set(accessLog);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AccessLog accessLog = accessLogThreadLocal.get();
        if (accessLog != null) {
            accessLog.setResponseHeaders(getResponseHeaders(response));
            accessLog.setResponseBody(getResponseBody(response));
            accessLog.setResponseTime(System.currentTimeMillis() - startTimeThreadLocal.get());
            accessLog.setResponseHttpCode(response.getStatus());
            accessLogger.logAccess(accessLog);
            accessLogThreadLocal.remove();
            startTimeThreadLocal.remove();
        }
    }

    private Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement().toLowerCase();
            if (!EXCLUDED_HEADERS.contains(headerName)) {
                headers.put(headerName, request.getHeader(headerName));
            }
        }
        return headers;
    }

    private String getRequestBody(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (IOException e) {
                    return "Error reading request body";
                }
            }
        }
        return "";
    }

    private Map<String, String> getResponseHeaders(HttpServletResponse response) {
        Map<String, String> headers = new HashMap<>();
        for (String headerName : response.getHeaderNames()) {
            String lowerHeaderName = headerName.toLowerCase();
            if (!EXCLUDED_HEADERS.contains(lowerHeaderName)) {
                headers.put(lowerHeaderName, response.getHeader(headerName));
            }
        }
        return headers;
    }

    private String getResponseBody(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (IOException e) {
                    return "Error reading response body";
                }
            }
        }
        return "";
    }
} 