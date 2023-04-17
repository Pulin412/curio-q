package com.app.curioq.qaservice.gateway;

import com.app.curioq.qaservice.feign.UserResponseDTO;

public interface QaGatewayService {
    UserResponseDTO fetchUserByEmail(String email, String jwtToken);
}
