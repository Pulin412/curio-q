package com.app.curioq.authservice.service;

import com.app.curioq.authservice.entity.Token;
import com.app.curioq.authservice.enums.TokenType;
import com.app.curioq.authservice.model.AuthenticationResponseDTO;
import com.app.curioq.authservice.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService{

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public AuthenticationServiceImpl(JwtService jwtService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public AuthenticationResponseDTO generateToken(String email) {
        log.info("AUTHENTICATION SERVICE ::: Incoming Request to generate token for {}", email);

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

        log.info("AUTHENTICATION SERVICE ::: Token generated Successfully");
        return AuthenticationResponseDTO.builder().token(generatedToken).build();
    }

    @Override
    public AuthenticationResponseDTO revokeAllTokens(String userId) {
        log.info("AUTHENTICATION SERVICE ::: Incoming Request to revoke all tokens for {}", userId);

        Optional<Token> optionalToken = tokenRepository.findByUserId(userId);

        if (optionalToken.isEmpty())
            return AuthenticationResponseDTO.builder().tokensRevoked(false).build();

        Token userToken = optionalToken.get();
        userToken.setExpired(true);
        userToken.setRevoked(true);
        tokenRepository.save(userToken);

        log.info("AUTHENTICATION SERVICE ::: All Tokens Revoked Successfully");
        return AuthenticationResponseDTO.builder().tokensRevoked(true).build();
    }
}
