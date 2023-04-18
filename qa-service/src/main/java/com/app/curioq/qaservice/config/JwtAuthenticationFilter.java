package com.app.curioq.qaservice.config;

import com.app.curioq.qaservice.exceptions.ErrorMessage;
import com.app.curioq.qaservice.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper = new ObjectMapper();
    private static final int TOKEN_PREFIX_LENGTH = "Bearer ".length();
    private static final DateTimeFormatter CUSTOM_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("QA SERVICE FILTER ::: Authenticating Incoming Request");
        final String authHeader = request.getHeader("Authorization");
        final String token;

        if (authHeader == null) {
            ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), "Token Header not found");
            writeToResponse(response, errorMessage);
        }

        try {
            token = Objects.requireNonNull(authHeader).substring(TOKEN_PREFIX_LENGTH);
            if(jwtService.isTokenValid(token, jwtService.extractUsername(token))){
                filterChain.doFilter(request, response);
            } else {
                ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), "Token is invalid");
                writeToResponse(response, errorMessage);
            }
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), e.getMessage());
            writeToResponse(response, errorMessage);
        }
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
