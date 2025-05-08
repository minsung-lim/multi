package com.example.apigw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Configuration
@EnableScheduling
public class DelayConfig {
    private volatile Map<String, Long> delayCache = Collections.emptyMap();

    @Bean
    public Supplier<Map<String, Long>> delayProvider() {
        return () -> delayCache;
    }

    @Scheduled(fixedRate = 60000)
    public void refreshDelayCache() {
        Map<String, Long> newCache = new HashMap<>();
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://postgres.internal:3521/delaydb", "user", "password");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT path_pattern, delay_ms FROM api_delay")) {
            while (rs.next()) {
                newCache.put(rs.getString("path_pattern"), rs.getLong("delay_ms"));
            }
            delayCache = newCache;
        } catch (Exception e) {
            // 로그 처리 필요
        }
    }
} 