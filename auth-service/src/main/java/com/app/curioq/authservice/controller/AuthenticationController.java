package com.app.curioq.authservice.controller;

import com.app.curioq.authservice.model.AuthenticationRequestDTO;
import com.app.curioq.authservice.model.AuthenticationResponseDTO;
import com.app.curioq.authservice.service.AuthenticationService;
import com.app.curioq.authservice.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/token")
    public ResponseEntity<AuthenticationResponseDTO> generate(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
        return ResponseEntity.ok(authenticationService.generateToken(authenticationRequestDTO.getEmail()));
    }

    @GetMapping("/getUser")
    public ResponseEntity<String> getUserId(@RequestParam String jwtToken) {
        return ResponseEntity.ok(jwtService.extractUsername(jwtToken));
    }

    @PostMapping("/revoke")
    public ResponseEntity<AuthenticationResponseDTO> revokeTokens(@RequestBody AuthenticationRequestDTO authenticationRequestDTO){
        return ResponseEntity.ok(authenticationService.revokeAllTokens(authenticationRequestDTO.getEmail()));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token, @RequestParam String email){
        return ResponseEntity.ok(authenticationService.validateToken(token, email));
    }

}
