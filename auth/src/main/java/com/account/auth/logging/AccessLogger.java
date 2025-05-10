package com.account.auth.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AccessLogger {
    private static final Logger logger = LoggerFactory.getLogger(AccessLogger.class);

    public void logAccess(AccessLog accessLog) {
        logger.info(accessLog.toString());
    }
} 