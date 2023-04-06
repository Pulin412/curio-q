package com.app.curioq.userservice.userservice.service;

import com.app.curioq.userservice.userservice.model.*;

import java.util.List;

public interface UserService {
    AuthenticationResponseDTO register(RegisterRequestDTO registerRequestDTO);
    AuthenticationResponseDTO login(AuthenticationRequestDTO authenticationRequestDTO);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUser(String email);
    void removeUser(String email);
    UserResponseDTO followUsers(UserFollowRequestDTO userFollowRequestDTO);
}
