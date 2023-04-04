package com.app.curioq.authservice.authservice.service;

import com.app.curioq.authservice.authservice.model.AuthenticationResponseDTO;

public interface AuthenticationService {
    AuthenticationResponseDTO generateToken(String userId);
    boolean revokeAllTokens(String userId);
}
