package com.app.curioq.userservice.userservice.gateway;

import com.app.curioq.userservice.userservice.config.AuthenticationGatewayConfig;
import com.app.curioq.userservice.userservice.entity.Users;
import com.app.curioq.userservice.userservice.model.AuthenticationRequestDTO;
import com.app.curioq.userservice.userservice.model.AuthenticationResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthenticationUserGatewayServiceImpl implements UserGatewayService {

    private final AuthenticationGatewayConfig authenticationGatewayConfig;

    @Override
    public String generateToken(Users savedUser) {
        ResponseEntity<AuthenticationResponseDTO> authenticationResponseEntity =
                authenticationGatewayConfig.tokenWebClient()
                        .post()
                        .uri(authenticationGatewayConfig.getGenerateTokenUrl())
                        .body(Mono.just(AuthenticationRequestDTO.builder().email(savedUser.getEmail()).build()), AuthenticationRequestDTO.class)
                        .retrieve()
                        .bodyToMono(AuthenticationResponseDTO.class)
                        .map(ResponseEntity::ok)
                        .block();
        if (authenticationResponseEntity != null && authenticationResponseEntity.getBody() != null)
            return authenticationResponseEntity.getBody().getToken();

        return null;
    }
}
