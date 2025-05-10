package com.account.apigw;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.account.com")
@TestPropertySource(properties = {
    "spring.cloud.gateway.discovery.locator.enabled=false",
    "spring.cloud.discovery.enabled=false"
})
public class ApiDocumentationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void healthCheck() {
        webTestClient.get()
                .uri("/actuator/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(WebTestClientRestDocumentation.document("health-check"));
    }

    @Test
    public void gatewayInfo() {
        webTestClient.get()
                .uri("/actuator/gateway/routes")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(WebTestClientRestDocumentation.document("gateway-routes"));
    }
} 