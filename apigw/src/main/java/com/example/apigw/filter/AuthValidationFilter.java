package com.example.apigw.filter;

import com.example.apigw.dto.ValidationResponse;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Component
public class AuthValidationFilter extends AbstractGatewayFilterFactory<AuthValidationFilter.Config> {

    private final WebClient webClient;

    public AuthValidationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder.baseUrl("http://metadata.internal").build();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            
            // 필수 헤더 체크
            if (!headers.containsKey("x-userid") || !headers.containsKey("x-appid")) {
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                return exchange.getResponse().setComplete();
            }

            String userId = headers.getFirst("x-userid");
            String appId = headers.getFirst("x-appid");

            // 메타데이터 서비스 호출하여 유효성 검증
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/validate")
                            .queryParam("userId", userId)
                            .queryParam("appId", appId)
                            .build())
                    .retrieve()
                    .bodyToMono(ValidationResponse.class)
                    .flatMap(response -> {
                        if (!response.isValid()) {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }
                        
                        // 필요한 경우 지연 추가
                        if (config.getDelay() > 0) {
                            try {
                                TimeUnit.MILLISECONDS.sleep(config.getDelay());
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                        
                        return chain.filter(exchange);
                    });
        };
    }

    public static class Config {
        private int delay = 0;

        public int getDelay() {
            return delay;
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }
    }
} 