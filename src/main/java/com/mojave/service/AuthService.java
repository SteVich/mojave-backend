package com.mojave.service;

import com.mojave.config.PropertiesConfig;
import com.mojave.dictionary.Role;
import com.mojave.model.User;
import com.mojave.model.UserProjectRole;
import com.mojave.payload.security.JwtAuthenticationResponse;
import com.mojave.payload.security.LoginRequest;
import com.mojave.payload.security.RoleResponse;
import com.mojave.payload.security.SignUpRequest;
import com.mojave.payload.security.TokenRequest;
import com.mojave.repository.UserRepository;
import com.mojave.security.JwtTokenProvider;
import com.mojave.security.UserPrincipal;
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
import java.util.List;
import java.util.stream.Collectors;

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

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        User user = userRepository.getOne(userId);

        String accessToken = createAccessToken(authentication, user);
        String refreshToken = createRefreshToken(authentication, user);

        return new JwtAuthenticationResponse(accessToken, refreshToken, userId);
    }

    public JwtAuthenticationResponse generateTokensFromUserEntity(User user) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null
        ));

        List<RoleResponse> roles = getRolesFromUser(user);

        String accessToken = tokenProvider.generateTokenFromId(user.getId(), properties.getExpirationAccessToken(), roles);
        String refreshToken = tokenProvider.generateTokenFromId(user.getId(), properties.getExpirationRefreshToken(), roles);

        return new JwtAuthenticationResponse(accessToken, refreshToken, user.getId());
    }

    public JwtAuthenticationResponse generateNewTokens(TokenRequest tokenRequest) {
        Long userId = tokenProvider.getUserIdFromJWT(tokenRequest.getRefreshToken());

        User user = userRepository.getOne(userId);
        List<RoleResponse> roles = getRolesFromUser(user);

        String newAccessToken = tokenProvider.generateTokenFromId(userId, properties.getExpirationAccessToken(), roles);
        String newRefreshToken = tokenProvider.generateTokenFromId(userId, properties.getExpirationRefreshToken(), roles);

        return new JwtAuthenticationResponse(newAccessToken, newRefreshToken, userId);
    }

    public String createAccessToken(Authentication authentication, User user) {
        List<RoleResponse> roles = getRolesFromUser(user);
        return tokenProvider.generateToken(authentication, properties.getExpirationAccessToken(), roles);
    }

    public String createRefreshToken(Authentication authentication, User user) {
        List<RoleResponse> roles = getRolesFromUser(user);
        return tokenProvider.generateToken(authentication, properties.getExpirationRefreshToken(), roles);
    }

    private List<RoleResponse> getRolesFromUser(User user) {
        return user.getProjectRoles().stream()
                .map(userProjectRole -> {
                    RoleResponse roleResponse = new RoleResponse();
                    roleResponse.setProjectId(userProjectRole.getId().getProjectId());
                    roleResponse.setRole(userProjectRole.getRole());

                    return roleResponse;
                }).collect(Collectors.toList());
    }
}
