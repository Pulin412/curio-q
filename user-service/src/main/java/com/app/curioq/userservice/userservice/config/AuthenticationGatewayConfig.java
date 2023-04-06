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

    @Value("${user.gateway.revokeTokens.url}")
    private String revokeTokensForUserUrl;

    @Value("${auth.secret.key}")
    private String secretKey;

    @Bean
    public WebClient tokenWebClient() {
        return WebClient.builder().baseUrl(generateTokenUrl).build();
    }

    @Bean
    public WebClient revokeTokensWebClient() {
        return WebClient.builder().baseUrl(revokeTokensForUserUrl).build();
    }
}
