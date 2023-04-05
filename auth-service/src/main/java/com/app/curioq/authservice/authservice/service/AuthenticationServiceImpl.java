package com.app.curioq.authservice.authservice.service;

import com.app.curioq.authservice.authservice.entity.Token;
import com.app.curioq.authservice.authservice.enums.TokenType;
import com.app.curioq.authservice.authservice.model.AuthenticationResponseDTO;
import com.app.curioq.authservice.authservice.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public AuthenticationServiceImpl(JwtService jwtService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public AuthenticationResponseDTO generateToken(String email) {
        String generatedToken = jwtService.generateToken(email);
        Token token = Token.builder()
                .userId(email)
                .token(generatedToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        Optional<Token> optionalToken = tokenRepository.findByUserId(email);
        optionalToken.ifPresent(tokenRepository::delete);
        tokenRepository.save(token);
        return AuthenticationResponseDTO.builder().token(generatedToken).build();
    }

    @Override
    public boolean revokeAllTokens(String userId) {
        Optional<Token> optionalToken = tokenRepository.findByUserId(userId);
        if (optionalToken.isEmpty())
            return false;
        Token userToken = optionalToken.get();
        userToken.setExpired(true);
        userToken.setRevoked(true);
        tokenRepository.save(userToken);
        return true;
    }
}
