package com.app.curioq.qaservice.gateway;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserGatewayServiceImpl implements UserGatewayService {

    @Override
    public UserResponseDTO fetchUserByEmail(String email, String jwtToken) {
        String url = "http://localhost:8081/api/v1/user?email=" + email;
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.AUTHORIZATION, jwtToken)
                .build()
                .get()
                .retrieve()
                .bodyToMono(UserResponseDTO.class)
                .block();
    }
}
