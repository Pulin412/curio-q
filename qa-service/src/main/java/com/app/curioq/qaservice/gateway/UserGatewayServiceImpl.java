package com.app.curioq.qaservice.gateway;

import com.app.curioq.qaservice.config.ApplicationGatewayConfig;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class UserGatewayServiceImpl implements UserGatewayService {

    private final ApplicationGatewayConfig applicationGatewayConfig;

    @Override
    public UserResponseDTO fetchUserByEmail(String email, String jwtToken) {
        String url = applicationGatewayConfig.getGetUserByEmailUrl() + email;
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.AUTHORIZATION, jwtToken)
                .build()
                .get()
                .retrieve()
                .bodyToMono(UserResponseDTO.class)
                .block();
    }

    @Override
    public UserResponseDTO fetchUserById(Long userId, String jwtToken) {
        return WebClient.builder()
                .baseUrl(applicationGatewayConfig.getGetUserUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, jwtToken)
                .build()
                .get()
                .uri("/user/{userId}", userId)
                .retrieve()
                .bodyToMono(UserResponseDTO.class)
                .block();
    }
}
