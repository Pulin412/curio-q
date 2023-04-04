package com.app.curioq.userservice.userservice.service;

import com.app.curioq.userservice.userservice.entity.Users;
import com.app.curioq.userservice.userservice.model.AuthenticationRequestDTO;
import com.app.curioq.userservice.userservice.model.RegisterRequestDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class ValidationService {

    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public void validateUser(RegisterRequestDTO registerRequestDTO) {
        if (registerRequestDTO.getFirstname() == null || registerRequestDTO.getFirstname().isEmpty()) {
            throw new IllegalArgumentException("Invalid First name");
        }
        if (registerRequestDTO.getEmail() == null || !registerRequestDTO.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            throw new IllegalArgumentException("Invalid Email Id");
        }
        if (registerRequestDTO.getPassword() == null) {
            throw new IllegalArgumentException("Invalid Password");
        }
    }

    public void validateLogin(AuthenticationRequestDTO authenticationRequestDTO) {
        if (StringUtils.isEmpty(authenticationRequestDTO)) {
            throw new IllegalArgumentException("Invalid Email Id");
        }
        if (StringUtils.isEmpty(authenticationRequestDTO.getPassword())) {
            throw new IllegalArgumentException("Invalid Password");
        }
    }

    public Claims getClaimsFromToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
