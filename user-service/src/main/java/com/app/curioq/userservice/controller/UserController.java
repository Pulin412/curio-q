package com.app.curioq.userservice.controller;

import com.app.curioq.userservice.model.*;
import com.app.curioq.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
    --------------------------------------------------------------------------------------------------------------------
            End points without Authentication
    --------------------------------------------------------------------------------------------------------------------
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO){
        return ResponseEntity.ok(userService.register(registerRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO authenticationRequestDTO){
        return ResponseEntity.ok(userService.login(authenticationRequestDTO));
    }

    /*
    --------------------------------------------------------------------------------------------------------------------
            End points accessible by all Roles (Logged in users)
    --------------------------------------------------------------------------------------------------------------------
     */

    @GetMapping("/user")
    public ResponseEntity<UserResponseDTO> getUser(@RequestParam String email){
        return ResponseEntity.ok(userService.getUser(email));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }


    @PostMapping("/follow")
    public ResponseEntity<UserResponseDTO> followUsers(@RequestBody UserFollowRequestDTO userFollowRequestDTO){
        return ResponseEntity.ok(userService.followUsers(userFollowRequestDTO));
    }

    /*
    --------------------------------------------------------------------------------------------------------------------
            End points accessible by ONLY ADMIN Role
    --------------------------------------------------------------------------------------------------------------------
     */
    @GetMapping("/admin/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/admin/user")
    public ResponseEntity<String> remove(@RequestParam String email){
        userService.removeUser(email);
        return ResponseEntity.ok("User deleted successfully");
    }

}
