package com.app.curioq.qaservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ApplicationGatewayConfig {

    @Value("${user.service.getUserByEmail.url}")
    private String getUserByEmailUrl;

    @Value("${user.service.url}")
    private String getUserUrl;
}
