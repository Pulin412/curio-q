package com.app.curioq.userservice.userservice.controller;

import com.app.curioq.userservice.userservice.model.AuthenticationRequestDTO;
import com.app.curioq.userservice.userservice.model.AuthenticationResponseDTO;
import com.app.curioq.userservice.userservice.model.RegisterRequestDTO;
import com.app.curioq.userservice.userservice.model.UserResponseDTO;
import com.app.curioq.userservice.userservice.service.UserService;
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

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO){
        return ResponseEntity.ok(userService.register(registerRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@RequestBody AuthenticationRequestDTO authenticationRequestDTO){
        return ResponseEntity.ok(userService.login(authenticationRequestDTO));
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponseDTO> getUser(@RequestParam String email){
        return ResponseEntity.ok(userService.getUser(email));
    }

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
