package com.app.curioq.userservice.userservice.config;

import com.app.curioq.userservice.userservice.enums.Role;
import com.app.curioq.userservice.userservice.model.AuthenticationRequestDTO;
import com.app.curioq.userservice.userservice.model.AuthenticationResponseDTO;
import com.app.curioq.userservice.userservice.model.RegisterRequestDTO;
import com.app.curioq.userservice.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserCommandLineRunner implements CommandLineRunner {

    private final UserService userService;

    public UserCommandLineRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        userService.register(RegisterRequestDTO.builder()
                .firstname("admin")
                .lastname("last1")
                .email("admin@gmail.com")
                .password("pass")
                .role(Role.ADMIN)
                .build());

        userService.register(RegisterRequestDTO.builder()
                .firstname("user1")
                .lastname("last2")
                .email("user1@gmail.com")
                .password("pass")
                .role(Role.USER)
                .build());

        userService.register(RegisterRequestDTO.builder()
                .firstname("user2")
                .lastname("last3")
                .email("user2@gmail.com")
                .password("pass")
                .role(Role.USER)
                .build());

        AuthenticationResponseDTO responseDTO = userService.login(
                AuthenticationRequestDTO.builder()
                        .email("admin@gmail.com")
                        .password("pass")
                        .build());

        log.info("USER COMMAND LINE RUNNER ::: Auto login for ADMIN, token - {}", responseDTO.getToken());
        log.info("USER COMMAND LINE RUNNER ::: Initial Data Load Successful");
    }
}
