package com.mojave.service;

import com.mojave.config.PropertiesConfig;
import com.mojave.dictionary.Role;
import com.mojave.model.User;
import com.mojave.payload.security.JwtAuthenticationResponse;
import com.mojave.payload.security.LoginRequest;
import com.mojave.payload.security.SignUpRequest;
import com.mojave.payload.security.TokenRequest;
import com.mojave.repository.UserRepository;
import com.mojave.security.JwtTokenProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtTokenProvider tokenProvider;
    AuthenticationManager authenticationManager;

    PropertiesConfig.JwtProperties properties;

    @Transactional(readOnly = true)
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public void registerUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);
    }

    @Transactional
    public JwtAuthenticationResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = createAccessToken(authentication);
        String refreshToken = createRefreshToken(authentication);
        Long userId = tokenProvider.getUserIdFromJWT(accessToken);

        return new JwtAuthenticationResponse(accessToken, refreshToken, userId);
    }

    public JwtAuthenticationResponse generateTokensFromUserEntity(User user) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null
        ));

        String accessToken = tokenProvider.generateTokenFromId(user.getId(), properties.getExpirationAccessToken());
        String refreshToken = tokenProvider.generateTokenFromId(user.getId(), properties.getExpirationRefreshToken());
        Long userId = tokenProvider.getUserIdFromJWT(accessToken);

        return new JwtAuthenticationResponse(accessToken, refreshToken, userId);
    }

    public JwtAuthenticationResponse generateNewTokens(TokenRequest tokenRequest) {
        Long userId = tokenProvider.getUserIdFromJWT(tokenRequest.getRefreshToken());

        String newAccessToken = tokenProvider.generateTokenFromId(userId, properties.getExpirationAccessToken());
        String newRefreshToken = tokenProvider.generateTokenFromId(userId, properties.getExpirationRefreshToken());

        return new JwtAuthenticationResponse(newAccessToken, newRefreshToken, userId);
    }

    public String createAccessToken(Authentication authentication) {
        return tokenProvider.generateToken(authentication, properties.getExpirationAccessToken());
    }

    public String createRefreshToken(Authentication authentication) {
        return tokenProvider.generateToken(authentication, properties.getExpirationRefreshToken());
    }

}
