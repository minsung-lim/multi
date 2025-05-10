package com.account.apigw.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.account.apigw.filter.AuthValidationFilter;
import com.account.apigw.filter.ProfileDelayFilter;

@Configuration
public class GatewayConfig {

    private final AuthValidationFilter authValidationFilter;
    private final ProfileDelayFilter profileDelayFilter;

    public GatewayConfig(AuthValidationFilter authValidationFilter, ProfileDelayFilter profileDelayFilter) {
        this.authValidationFilter = authValidationFilter;
        this.profileDelayFilter = profileDelayFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("profile_route", r -> r
                        .path("/profile/**")
                        .filters(f -> f.stripPrefix(1)
                                       .filter(profileDelayFilter.apply(new ProfileDelayFilter.Config())))
                        .uri("http://profile.internal"))
                .route("auth_route", r -> r
                        .path("/auth/{code}")
                        .filters(f -> f
                                .stripPrefix(1)
                                .filter(authValidationFilter.apply(new AuthValidationFilter.Config())))
                        .uri("http://auth.internal"))
                .build();
    }
} 