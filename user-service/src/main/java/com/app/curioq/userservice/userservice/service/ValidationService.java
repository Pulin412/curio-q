package com.app.curioq.userservice.userservice.service;

import com.app.curioq.userservice.userservice.config.AuthenticationGatewayConfig;
import com.app.curioq.userservice.userservice.exceptions.ValidationException;
import com.app.curioq.userservice.userservice.model.AuthenticationRequestDTO;
import com.app.curioq.userservice.userservice.model.RegisterRequestDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.app.curioq.userservice.userservice.utils.UserServiceConstants.*;

@Service
public class ValidationService {

    private final AuthenticationGatewayConfig authenticationGatewayConfig;

    public ValidationService(AuthenticationGatewayConfig authenticationGatewayConfig) {
        this.authenticationGatewayConfig = authenticationGatewayConfig;
    }

    public void validateUser(RegisterRequestDTO registerRequestDTO) {
        if (registerRequestDTO.getFirstname() == null || registerRequestDTO.getFirstname().isEmpty()) {
            throw new ValidationException(EXCEPTION_INVALID_FIRST_NAME_MESSAGE);
        }
        if (registerRequestDTO.getEmail() == null || !registerRequestDTO.getEmail().matches(VALIDATION_EMAIL_REGEX)) {
            throw new ValidationException(EXCEPTION_INVALID_EMAIL_MESSAGE);
        }
        if (registerRequestDTO.getPassword() == null) {
            throw new ValidationException(EXCEPTION_INVALID_PASSWORD_MESSAGE);
        }
    }

    public void validateLogin(AuthenticationRequestDTO authenticationRequestDTO) {
        if (StringUtils.isEmpty(authenticationRequestDTO)) {
            throw new ValidationException(EXCEPTION_INVALID_EMAIL_MESSAGE);
        }
        if (StringUtils.isEmpty(authenticationRequestDTO.getPassword())) {
            throw new ValidationException(EXCEPTION_INVALID_PASSWORD_MESSAGE);
        }
    }

    public Claims getClaimsFromToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(authenticationGatewayConfig.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
