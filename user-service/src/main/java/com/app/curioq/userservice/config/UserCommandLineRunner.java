package com.app.curioq.userservice.config;

import com.app.curioq.userservice.enums.Role;
import com.app.curioq.userservice.model.AuthenticationRequestDTO;
import com.app.curioq.userservice.model.AuthenticationResponseDTO;
import com.app.curioq.userservice.model.RegisterRequestDTO;
import com.app.curioq.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@Slf4j
public class UserCommandLineRunner implements CommandLineRunner {

    private final UserService userService;

    public UserCommandLineRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        if(CollectionUtils.isEmpty(userService.getAllUsers())){
            userService.register(createUser("user1", "last1", "user1@gmail.com", Role.USER.name()));
            userService.register(createUser("user2", "last2", "user2@gmail.com", Role.USER.name()));
            userService.register(createUser("user3", "last3", "user3@gmail.com", Role.USER.name()));
            userService.register(createUser("user4", "last4", "user4@gmail.com", Role.USER.name()));
            userService.register(createUser("user5", "last5", "user5@gmail.com", Role.USER.name()));
            userService.register(createUser("user6", "last6", "user6@gmail.com", Role.USER.name()));
            userService.register(createUser("user7", "last7", "user7@gmail.com", Role.USER.name()));
            userService.register(createUser("user8", "last8", "user8@gmail.com", Role.USER.name()));
            userService.register(createUser("user9", "last9", "user9@gmail.com", Role.USER.name()));
            userService.register(createUser("user10", "last10", "user10@gmail.com", Role.USER.name()));

            userService.register(createUser("admin", "last1", "admin@gmail.com", Role.ADMIN.name()));

            AuthenticationResponseDTO responseDTO = userService.login(
                    AuthenticationRequestDTO.builder()
                            .email("admin@gmail.com")
                            .password("pass")
                            .build());

            log.info("USER COMMAND LINE RUNNER ::: Auto login for ADMIN, token - {}", responseDTO.getToken());
            log.info("USER COMMAND LINE RUNNER ::: Initial Data Load Successful");
        }
    }

    private RegisterRequestDTO createUser(String firstName, String lastName, String email, String role){
        return RegisterRequestDTO.builder()
                .firstname(firstName)
                .lastname(lastName)
                .email(email)
                .password("pass")
                .role(Role.valueOf(role))
                .build();
    }
}
