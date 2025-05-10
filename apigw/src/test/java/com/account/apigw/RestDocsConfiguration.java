package com.account.apigw;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentationConfigurer;

@TestConfiguration
public class RestDocsConfiguration {

    @Bean
    public WebTestClientRestDocumentationConfigurer restDocumentationConfigurer(
            RestDocumentationContextProvider restDocumentationContextProvider) {
        return WebTestClientRestDocumentation.documentationConfiguration(restDocumentationContextProvider);
    }
} 