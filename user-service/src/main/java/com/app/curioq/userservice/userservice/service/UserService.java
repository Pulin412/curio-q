package com.app.curioq.userservice.userservice.service;

import com.app.curioq.userservice.userservice.entity.Users;
import com.app.curioq.userservice.userservice.model.AuthenticationResponseDTO;
import com.app.curioq.userservice.userservice.model.AuthenticationRequestDTO;
import com.app.curioq.userservice.userservice.model.RegisterRequestDTO;
import com.app.curioq.userservice.userservice.model.UserResponseDTO;

import java.util.List;

public interface UserService {
    AuthenticationResponseDTO register(RegisterRequestDTO registerRequestDTO);
    AuthenticationResponseDTO login(AuthenticationRequestDTO authenticationRequestDTO);
    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUser(String email);
}
