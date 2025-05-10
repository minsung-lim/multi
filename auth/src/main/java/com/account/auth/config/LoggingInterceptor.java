package com.account.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestBody = "";
        if (request.getContentType() != null && request.getContentType().contains("application/json")) {
            try {
                ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
                requestBody = new String(requestWrapper.getContentAsByteArray());
            } catch (Exception e) {
                log.error("Error reading request body", e);
            }
        }

        log.info("Request - Method: {}, URI: {}, Headers: {}, Body: {}",
                request.getMethod(),
                request.getRequestURI(),
                Collections.list(request.getHeaderNames()).stream()
                        .collect(Collectors.toMap(
                                headerName -> headerName,
                                request::getHeader
                        )),
                requestBody);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info("Response - Status: {}, Headers: {}",
                response.getStatus(),
                response.getHeaderNames().stream()
                        .collect(Collectors.toMap(
                                headerName -> headerName,
                                response::getHeader
                        )));
    }
} 