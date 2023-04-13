package com.app.curioq.userservice.service;

import com.app.curioq.userservice.exceptions.ValidationException;
import com.app.curioq.userservice.model.AuthenticationRequestDTO;
import com.app.curioq.userservice.model.RegisterRequestDTO;
import com.app.curioq.userservice.model.UserFollowRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.app.curioq.userservice.utils.UserServiceConstants.*;

@Service
@Slf4j
public class ValidationService {

    public void validateUser(RegisterRequestDTO registerRequestDTO) {
        log.info("VALIDATION SERVICE ::: Validating User details for Registration request");

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
        log.info("VALIDATION SERVICE ::: Validating User details for Login request");

        if (StringUtils.isEmpty(authenticationRequestDTO)) {
            throw new ValidationException(EXCEPTION_INVALID_EMAIL_MESSAGE);
        }
        if (StringUtils.isEmpty(authenticationRequestDTO.getPassword())) {
            throw new ValidationException(EXCEPTION_INVALID_PASSWORD_MESSAGE);
        }
    }

    public void validateUserFollowerRequest(UserFollowRequestDTO userFollowRequestDTO) {
        log.info("VALIDATION SERVICE ::: Validating User Follow request");

        long followerId = userFollowRequestDTO.getFollowerId();
        long followeeId = userFollowRequestDTO.getFolloweeId();

        if(followerId <= 0L || followeeId <= 0L || followerId == followeeId)
            throw new ValidationException(EXCEPTION_INVALID_FOLLOW_REQUEST_MESSAGE);
    }
}
