package com.app.curioq.userservice.userservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Getter
public class AuthenticationGatewayConfig {

    @Value("${user.gateway.token.generate.url}")
    private String generateTokenUrl;

    @Value("${user.gateway.getUserFromToken.url}")
    private String getUserFromTokenUrl;

    @Value("${auth.secret.key}")
    private String secretKey;

    @Bean
    public WebClient tokenWebClient() {
        return WebClient.builder().baseUrl(generateTokenUrl).build();
    }

    @Bean
    public WebClient userWebClient() {
        return WebClient.builder().baseUrl(getUserFromTokenUrl).build();
    }

    @Bean
    public WebClient isTokenValidWebClient() {
        return WebClient.builder().baseUrl(getUserFromTokenUrl).build();
    }
}
