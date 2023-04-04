package com.app.curioq.userservice.userservice.gateway;

import com.app.curioq.userservice.userservice.entity.Users;

public interface UserGatewayService {
    String generateToken(Users savedUser);
}
