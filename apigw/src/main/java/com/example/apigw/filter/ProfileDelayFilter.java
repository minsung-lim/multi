package com.example.apigw.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class ProfileDelayFilter extends AbstractGatewayFilterFactory<ProfileDelayFilter.Config> {
    private final Supplier<Map<String, Long>> delayProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public ProfileDelayFilter(Supplier<Map<String, Long>> delayProvider) {
        super(Config.class);
        this.delayProvider = delayProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();
            long delay = 0;
            for (Map.Entry<String, Long> entry : delayProvider.get().entrySet()) {
                if (pathMatcher.match(entry.getKey(), path)) {
                    delay = entry.getValue();
                    break;
                }
            }
            return Mono.delay(java.time.Duration.ofMillis(delay)).then(chain.filter(exchange));
        };
    }

    public static class Config {}
} 