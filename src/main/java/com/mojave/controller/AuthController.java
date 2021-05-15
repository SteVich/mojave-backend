package com.mojave.controller;

import com.mojave.payload.response.ApiResponse;
import com.mojave.payload.security.JwtAuthenticationResponse;
import com.mojave.payload.security.LoginRequest;
import com.mojave.payload.security.SignUpRequest;
import com.mojave.payload.security.TokenRequest;
import com.mojave.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {

    AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        ApiResponse apiResponse;

        if (authService.existsByUsername(signUpRequest.getUsername())) {
            apiResponse = new ApiResponse(false, "Username  is already taken!");
        } else if (authService.existsByEmail(signUpRequest.getEmail())) {
            apiResponse = new ApiResponse(false, "Email  is already taken!");
        } else {
            apiResponse = new ApiResponse(true, "User registered successfully");
            authService.registerUser(signUpRequest);
        }

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.loginUser(loginRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshJwtToken(@Valid @RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(authService.generateNewTokens(tokenRequest));
    }

    @GetMapping("/email-exists")
    public ResponseEntity<Boolean> checkIfEmailExists(@RequestParam String email) {
        return ResponseEntity.ok(authService.existsByEmail(email));
    }

    @GetMapping("/username-exists")
    public ResponseEntity<Boolean> checkIfUsernameExists(@RequestParam String username) {
        return ResponseEntity.ok(authService.existsByUsername(username));
    }
}

