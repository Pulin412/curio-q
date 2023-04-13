package com.app.curioq.userservice.gateway;

import com.app.curioq.userservice.entity.Users;

public interface UserGatewayService {
    String generateToken(Users savedUser);
    Boolean revokeTokens(String email);
}
