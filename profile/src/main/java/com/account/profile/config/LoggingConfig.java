package com.account.profile.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

@Slf4j
@Configuration
public class LoggingConfig extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        long timeTaken = System.currentTimeMillis() - startTime;

        String requestBody = getStringValue(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = getStringValue(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding());
        String requestHeaders = getHeadersAsString(requestWrapper);
        String responseHeaders = getHeadersAsString(response);

        log.info(
                "REQUEST: {} {} | REQUEST HEADERS: {} | REQUEST BODY: {} | RESPONSE STATUS: {} | RESPONSE HEADERS: {} | RESPONSE BODY: {} | TIME TAKEN: {}ms",
                request.getMethod(),
                request.getRequestURI(),
                requestHeaders,
                requestBody,
                response.getStatus(),
                responseHeaders,
                responseBody,
                timeTaken
        );

        responseWrapper.copyBodyToResponse();
    }

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            return "[Unknown]";
        }
    }

    private String getHeadersAsString(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.append(name).append(": ").append(request.getHeader(name)).append("; ");
        }
        return headers.toString();
    }

    private String getHeadersAsString(HttpServletResponse response) {
        StringBuilder headers = new StringBuilder();
        for (String name : response.getHeaderNames()) {
            headers.append(name).append(": ").append(response.getHeader(name)).append("; ");
        }
        return headers.toString();
    }
} 