package com.app.curioq.userservice.userservice.config;

import com.app.curioq.userservice.userservice.exceptions.ErrorMessage;
import com.app.curioq.userservice.userservice.service.ValidationService;
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
import java.util.Date;

import static com.app.curioq.userservice.userservice.utils.UserServiceConstants.CUSTOM_DATE_TIME_FORMATTER;
import static com.app.curioq.userservice.userservice.utils.UserServiceConstants.TOKEN_PREFIX_LENGTH;

@Component
@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final ValidationService validationService;
    private final AuthenticationGatewayConfig authenticationGatewayConfig;
    private ObjectMapper mapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("AUTHENTICATION FILTER ::: Authentication Incoming Request");
        final String authHeader = request.getHeader("Authorization");
        final String token;

        if(authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            token = authHeader.substring(TOKEN_PREFIX_LENGTH);
            String userEmail = fetchEmailFromToken(token);

            if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                Claims claims = validationService.getClaimsFromToken(token);
                if(claims.getExpiration().after(new Date())){
                    log.info("AUTHENTICATION FILTER ::: User authenticated with Authority {}", userDetails.getAuthorities());

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e){
            ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), e.getMessage());
            writeToResponse(response, errorMessage);
        }
    }

    private String fetchEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(authenticationGatewayConfig.getSecretKey())
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
