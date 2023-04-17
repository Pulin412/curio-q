package com.app.curioq.qaservice.gateway;

public interface QaGatewayService {
    UserResponseDTO fetchUserByEmail(String email, String jwtToken);
}
