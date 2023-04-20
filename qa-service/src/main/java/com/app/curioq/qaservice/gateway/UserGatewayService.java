package com.app.curioq.qaservice.gateway;

public interface UserGatewayService {
    UserResponseDTO fetchUserByEmail(String email, String jwtToken);
    UserResponseDTO fetchUserById(Long userId, String jwtToken);
}
