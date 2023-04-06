package com.app.curioq.userservice.userservice.service;

import com.app.curioq.userservice.userservice.entity.Users;
import com.app.curioq.userservice.userservice.exceptions.InvalidLoginException;
import com.app.curioq.userservice.userservice.exceptions.UserAlreadyPresentException;
import com.app.curioq.userservice.userservice.gateway.UserGatewayService;
import com.app.curioq.userservice.userservice.model.AuthenticationRequestDTO;
import com.app.curioq.userservice.userservice.model.AuthenticationResponseDTO;
import com.app.curioq.userservice.userservice.model.RegisterRequestDTO;
import com.app.curioq.userservice.userservice.model.UserResponseDTO;
import com.app.curioq.userservice.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.app.curioq.userservice.userservice.utils.UserServiceConstants.EXCEPTION_INVALID_LOGIN_MESSAGE;
import static com.app.curioq.userservice.userservice.utils.UserServiceConstants.EXCEPTION_USER_ALREADY_PRESENT_MESSAGE;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserGatewayService userGatewayService;
    private final ValidationService validationService;
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        validationService.validateUser(registerRequestDTO);

        Optional<Users> optionalUser = userRepository.findByEmail(registerRequestDTO.getEmail());
        if(optionalUser.isPresent()){
            throw new UserAlreadyPresentException(EXCEPTION_USER_ALREADY_PRESENT_MESSAGE);
        }
        Users savedUser = userRepository.save(mapUserDtoToEntity(registerRequestDTO));
        String token = userGatewayService.generateToken(savedUser);
        saveToken(savedUser, token);

        return AuthenticationResponseDTO.builder()
                .token(token)
                .build();
    }

    private Users mapUserDtoToEntity(RegisterRequestDTO registerRequestDTO) {
        return Users.builder()
                .firstname(registerRequestDTO.getFirstname())
                .lastname(registerRequestDTO.getLastname())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .role(registerRequestDTO.getRole())
                .build();
    }

    private void saveToken(Users savedUser, String token) {
        savedUser.setToken(token);
        userRepository.save(savedUser);
    }

    @Override
    public AuthenticationResponseDTO login(AuthenticationRequestDTO authenticationRequestDTO) {
        validationService.validateLogin(authenticationRequestDTO);

        Optional<Users> optionalUser = userRepository.findByEmail(
                authenticationRequestDTO.getEmail());

        if(optionalUser.isPresent()){
            Users userFromDb = optionalUser.get();
            String encodedPassword = userFromDb.getPassword();

            if(passwordEncoder.matches(authenticationRequestDTO.getPassword(), encodedPassword)){
                String token = "";

            /*
                - If user already has a token and its not expired, return token
                - If user doesn't have a token or if the token is expired, remove all user tokens (expired)
                   and generate a new token.
             */
                Claims claims = validationService.getClaimsFromToken(userFromDb.getToken());

                if(userFromDb.getToken() != null && userFromDb.getEmail().equalsIgnoreCase(claims.getSubject()) && claims.getExpiration().after(new Date())){
                    token = userFromDb.getToken();
                } else {

                    token = userGatewayService.generateToken(userFromDb);
                    saveToken(userFromDb, token);
                }

                return AuthenticationResponseDTO.builder().token(userFromDb.getToken()).build();
            } else {
                throw new InvalidLoginException(EXCEPTION_INVALID_LOGIN_MESSAGE);
            }
        } else {
            throw new InvalidLoginException(EXCEPTION_INVALID_LOGIN_MESSAGE);
        }
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<Users> responseList = userRepository.findAll();
        return responseList.stream().map(users ->
            UserResponseDTO.builder()
                    .firstname(users.getFirstname())
                    .lastname(users.getLastname())
                    .email(users.getEmail())
                    .password(users.getPassword())
                    .role(users.getRole().name())
                    .build()).collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUser(String email) {
        Optional<Users> optionalUser = userRepository.findByEmail(email);
        UserResponseDTO.UserResponseDTOBuilder userResponseDTOBuilder = UserResponseDTO.builder();

        if(optionalUser.isPresent()){
            Users userFromDb = optionalUser.get();
            userResponseDTOBuilder
                    .firstname(userFromDb.getFirstname())
                    .lastname(userFromDb.getLastname())
                    .email(userFromDb.getEmail())
                    .password(userFromDb.getPassword())
                    .role(userFromDb.getRole().name());
        }
        return userResponseDTOBuilder.build();
    }
}
