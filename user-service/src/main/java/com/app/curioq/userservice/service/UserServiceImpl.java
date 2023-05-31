package com.app.curioq.userservice.service;

import com.app.curioq.securitylib.service.JwtValidationService;
import com.app.curioq.userservice.entity.Users;
import com.app.curioq.userservice.exceptions.*;
import com.app.curioq.userservice.gateway.UserGatewayService;
import com.app.curioq.userservice.model.*;
import com.app.curioq.userservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.app.curioq.userservice.utils.UserServiceConstants.*;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserGatewayService userGatewayService;
    private final ValidationService validationService;
    private final JwtValidationService jwtValidationService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        validationService.validateUser(registerRequestDTO);

        Optional<Users> optionalUser = userRepository.findByEmail(registerRequestDTO.getEmail());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyPresentException(EXCEPTION_USER_ALREADY_PRESENT_MESSAGE);
        }
        Users savedUser = userRepository.save(mapUserDtoToEntity(registerRequestDTO));

        String token = userGatewayService.generateToken(savedUser);

        if (token == null)
            throw new GatewayException(EXCEPTION_GATEWAY_MESSAGE);

        log.info("USER SERVICE ::: Token generated for {}", savedUser.getEmail());
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

        if (optionalUser.isPresent()) {
            log.info("USER SERVICE ::: Found User From DB");
            Users userFromDb = optionalUser.get();
            String encodedPassword = userFromDb.getPassword();

            if (passwordEncoder.matches(authenticationRequestDTO.getPassword(), encodedPassword)) {
                String token = "";

            /*
                - If user already has a token and its not expired, return token
                - If user doesn't have a token or if the token is expired, remove all user tokens (expired)
                   and generate a new token.
             */
                try {
                    Claims claims = jwtValidationService.getClaimsFromToken(userFromDb.getToken());
                    if (userFromDb.getToken() != null && userFromDb.getEmail().equalsIgnoreCase(claims.getSubject())) {
                        token = userFromDb.getToken();
                    }
                } catch (ExpiredJwtException e) {
                    log.info("USER SERVICE ::: Token not available, generating new token for {}", userFromDb.getEmail());
                    token = userGatewayService.generateToken(userFromDb);
                    saveToken(userFromDb, token);
                }

                return AuthenticationResponseDTO.builder().token(token).build();
            } else {
                throw new InvalidLoginException(EXCEPTION_INVALID_LOGIN_MESSAGE);
            }
        } else {
            throw new InvalidLoginException(EXCEPTION_USER_NOT_PRESENT_MESSAGE);
        }
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<Users> responseList = userRepository.findAll();
        return responseList.stream().map(users ->
                UserResponseDTO.builder()
                        .userId(users.getId())
                        .firstname(users.getFirstname())
                        .lastname(users.getLastname())
                        .email(users.getEmail())
                        .password(users.getPassword())
                        .role(users.getRole().name())
                        .followers(users.getFollowers().stream().map(Users::getEmail).collect(Collectors.toUnmodifiableSet()))
                        .following(users.getFollowing().stream().map(Users::getEmail).collect(Collectors.toUnmodifiableSet()))
                        .likes(users.getLikes().stream().map(Users::getEmail).collect(Collectors.toUnmodifiableSet()))
                        .build()).collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUser(String email) {
        Optional<Users> optionalUser = userRepository.findByEmail(email);
        UserResponseDTO.UserResponseDTOBuilder userResponseDTOBuilder = UserResponseDTO.builder();

        if (optionalUser.isPresent()) {
            Users userFromDb = optionalUser.get();
            userResponseDTOBuilder
                    .userId(userFromDb.getId())
                    .firstname(userFromDb.getFirstname())
                    .lastname(userFromDb.getLastname())
                    .email(userFromDb.getEmail())
                    .password(userFromDb.getPassword())
                    .role(userFromDb.getRole().name())
                    .followers(userFromDb.getFollowers().stream().map(Users::getEmail).collect(Collectors.toUnmodifiableSet()))
                    .following(userFromDb.getFollowing().stream().map(Users::getEmail).collect(Collectors.toUnmodifiableSet()))
                    .likes(userFromDb.getLikes().stream().map(Users::getEmail).collect(Collectors.toUnmodifiableSet()));
        }
        return userResponseDTOBuilder.build();
    }

    @Override
    public void removeUser(String email) {
        if (email == null || !email.matches(VALIDATION_EMAIL_REGEX))
            throw new ValidationException(EXCEPTION_INVALID_EMAIL_MESSAGE);

        Optional<Users> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            if (userGatewayService.revokeTokens(email)) {

                log.info("USER SERVICE ::: All User Tokens revoked for {}", optionalUser.get().getEmail());
                userRepository.delete(optionalUser.get());
            } else {
                throw new GatewayException(EXCEPTION_GATEWAY_MESSAGE);
            }
        } else {
            throw new GenericException(EXCEPTION_USER_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    public UserResponseDTO followUsers(UserFollowRequestDTO userFollowRequestDTO) {
        validationService.validateUserFollowerRequest(userFollowRequestDTO);

        long followerId = userFollowRequestDTO.getFollowerId();
        long followeeId = userFollowRequestDTO.getFolloweeId();

        Optional<Users> optionalFollower = Optional.ofNullable(
                userRepository.findById(followerId)
                        .orElseThrow(() -> new UsernameNotFoundException(EXCEPTION_USER_NOT_PRESENT_MESSAGE + " : " + followerId)));

        Optional<Users> optionalFollowee = Optional.ofNullable(
                userRepository.findById(followeeId)
                        .orElseThrow(() -> new UsernameNotFoundException(EXCEPTION_USER_NOT_PRESENT_MESSAGE + " : " + followeeId)));

        Users followee = optionalFollowee.get();
        Users follower = optionalFollower.get();

        follower.getFollowing().add(followee);
        userRepository.save(follower);

        return UserResponseDTO.builder().response(follower.getEmail() + " is now following " + followee.getEmail()).build();
    }

    @Override
    public UserResponseDTO getUserById(Long userId) {
        Users userFromDb = userRepository.findById(userId)
                .orElseThrow(() -> new GenericException(EXCEPTION_USER_NOT_PRESENT_MESSAGE));
        return UserResponseDTO.builder()
                .userId(userFromDb.getId())
                .email(userFromDb.getEmail())
                .firstname(userFromDb.getFirstname())
                .lastname(userFromDb.getLastname())
                .role(userFromDb.getRole().name())
                .followers(userFromDb.getFollowers().stream().map(Users::getEmail).collect(Collectors.toUnmodifiableSet()))
                .following(userFromDb.getFollowing().stream().map(Users::getEmail).collect(Collectors.toUnmodifiableSet()))
                .likes(userFromDb.getLikes().stream().map(Users::getEmail).collect(Collectors.toUnmodifiableSet()))
                .password(userFromDb.getPassword())
                .build();
    }

    @Override
    public UserResponseDTO likeUser(UserLikeRequestDTO userLikeRequestDTO) {
        long userTobeLikedId = userLikeRequestDTO.getBeingLikedId();
        long userLikingId = userLikeRequestDTO.getLikeId();

        Users user = userRepository.findById(userLikingId).orElseThrow(() -> new GenericException(EXCEPTION_USER_NOT_PRESENT_MESSAGE));
        Users userToLike = userRepository.findById(userTobeLikedId).orElseThrow(() -> new GenericException(EXCEPTION_USER_NOT_PRESENT_MESSAGE));

        user.getLikes().add(userToLike);
        userRepository.save(user);

        return UserResponseDTO.builder().response(user.getEmail() + " liked " + userToLike.getEmail()).build();
    }
}
