package com.app.curioq.userservice.userservice.gateway;

import com.app.curioq.userservice.userservice.config.AuthenticationGatewayConfig;
import com.app.curioq.userservice.userservice.entity.Users;
import com.app.curioq.userservice.userservice.model.AuthenticationRequestDTO;
import com.app.curioq.userservice.userservice.model.AuthenticationResponseDTO;
import com.app.curioq.userservice.userservice.service.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthenticationUserGatewayServiceImpl implements UserGatewayService {

    private final AuthenticationGatewayConfig authenticationGatewayConfig;
    private final UserDetailsService userDetailsService;
    private final ValidationService validationService;

//        String url = String.format("%s?jwtToken=%s", authenticationGatewayConfig.getGetUserFromTokenUrl(), token);
//        ResponseEntity<String> responseEntity = authenticationGatewayConfig.userWebClient()
//                .get()
//                .uri(url)
//                .retrieve()
//                .bodyToMono(String.class)
//                .map(ResponseEntity::ok)
//                .block();
//
//        String userId = responseEntity.getBody();


//        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);
//
////            ResponseEntity<AuthenticationResponseDTO> authenticationResponseEntity =
////                    authenticationGatewayConfig.tokenWebClient()
////                            .post()
////                            .uri(authenticationGatewayConfig.getGenerateTokenUrl())
////                            .body(Mono.just(AuthenticationRequestDTO.builder().userId(userId).build()), AuthenticationRequestDTO.class)
////                            .retrieve()
////                            .bodyToMono(AuthenticationResponseDTO.class)
////                            .map(ResponseEntity::ok)
////                            .block();
//
//            ResponseEntity<Boolean> isTokenValidResponse =
//                    authenticationGatewayConfig.isTokenValidWebClient()
//                        .get()
//                        .uri(url)
//                        .retrieve()
//                        .bodyToMono(Boolean.class)
//                        .map(ResponseEntity::ok)
//                        .block();
//
//            var isTokenValid = tokenRepository.findByToken(jwt)
//                    .map(t -> !t.isExpired() && !t.isRevoked())
//                    .orElse(false);
//
//            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
//                UsernamePasswordAuthenticationToken authenticationToken =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//        }

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
