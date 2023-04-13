package com.app.curioq.securitylib.service;

import com.app.curioq.securitylib.utils.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtValidationService {

    public Claims getClaimsFromToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(SecurityConstants.SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
