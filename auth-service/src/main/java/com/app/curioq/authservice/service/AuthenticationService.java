package com.app.curioq.authservice.service;

import com.app.curioq.authservice.model.AuthenticationResponseDTO;

public interface AuthenticationService {
    AuthenticationResponseDTO generateToken(String userId);
    AuthenticationResponseDTO revokeAllTokens(String userId);
}
