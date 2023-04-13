package com.app.curioq.securitylib.config;

import com.app.curioq.securitylib.exception.ErrorMessage;
import com.app.curioq.securitylib.service.JwtValidationService;
import com.app.curioq.securitylib.utils.SecurityConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtValidationService jwtValidationService;
    private ObjectMapper mapper;

    public static final int TOKEN_PREFIX_LENGTH = "Bearer ".length();
    public static final DateTimeFormatter CUSTOM_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("SECURITY-LIB AUTH FILTER ::: Authentication Incoming Request");
        final String authHeader = request.getHeader("Authorization");
        final String token;

        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            token = authHeader.substring(TOKEN_PREFIX_LENGTH);
            String userEmail = fetchEmailFromToken(token);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                Claims claims = jwtValidationService.getClaimsFromToken(token);

                if (claims.getExpiration().after(new Date())) {
                    log.info("SECURITY-LIB AUTH FILTER ::: User authenticated with Authority {}", userDetails.getAuthorities());

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), e.getMessage());
            writeToResponse(response, errorMessage);
        }
    }

    private String fetchEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SecurityConstants.SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    private void writeToResponse(HttpServletResponse response, ErrorMessage errorMessage) throws IOException {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(CUSTOM_DATE_TIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(CUSTOM_DATE_TIME_FORMATTER));
        mapper.registerModule(javaTimeModule);

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        OutputStream out = response.getOutputStream();
        if (!response.isCommitted()) {
            out.write(mapper.writeValueAsString((errorMessage)).getBytes());
        }
        out.flush();
        out.close();
    }
}
