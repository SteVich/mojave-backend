package com.mojave.security.google;


import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.mojave.config.PropertiesConfig;
import com.mojave.dictionary.Role;
import com.mojave.model.Project;
import com.mojave.model.User;
import com.mojave.model.UserProjectRole;
import com.mojave.payload.security.JwtAuthenticationResponse;
import com.mojave.repository.InviteRepository;
import com.mojave.repository.ProjectRepository;
import com.mojave.repository.UserRepository;
import com.mojave.service.AuthService;
import com.mojave.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoogleLoginService {

    static final String USER_INFO_EMAIL = "https://www.googleapis.com/auth/userinfo.email";
    static final String USER_INFO_PROFILE = "https://www.googleapis.com/auth/userinfo.profile";
    static final String GOOGLE_ACCOUNT_STATE = "/profile";

    PropertiesConfig.GoogleProperties googleProperties;
    UserService userService;
    AuthService authService;
    InviteRepository inviteRepository;
    ProjectRepository projectRepository;
    UserRepository userRepository;

    @Transactional
    public JwtAuthenticationResponse findAndAuthenticateUser(StringBuffer stringBuffer) {
        AuthorizationCodeResponseUrl authResponse = checkAuthorizationResponse(stringBuffer);
        User user = findUser(authResponse.getCode());

        return authService.generateTokensFromUserEntity(user);
    }

    private AuthorizationCodeResponseUrl checkAuthorizationResponse(StringBuffer stringBuffer) {
        AuthorizationCodeResponseUrl authResponse = new AuthorizationCodeResponseUrl(stringBuffer.toString());

        if (Objects.nonNull(authResponse.getError())) {
            throw new AccessDeniedException("Access denied!");
        }

        return authResponse;
    }

    private User findUser(String code) {
        GoogleTokenResponse tokenResponse = getGoogleTokenResponse(code);
        GoogleIdToken.Payload payload = getGoogleTokenPayload(tokenResponse);

        String email = payload.getEmail();

        User user;
        Optional<User> optionalUser = userService.findOptionalUserByEmail(email).stream().findFirst();

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = createNewUser(email, payload);
            setInvitedInvitedUserToProject(email, user);
        }

        return user;
    }

    private void setInvitedInvitedUserToProject(String email, User user) {
        inviteRepository.findByEmail(email).ifPresent(invite -> {
            if (invite.getDueDate().isAfter(LocalDateTime.now())) {
                Project project = invite.getProject();

                user.getProjects().add(project);
                userService.setRole(project, user, Role.ROLE_DEVELOPER);

                project.getUsers().add(user);

                projectRepository.save(project);
                userRepository.save(user);
            }
            inviteRepository.delete(invite);
        });
    }

    private User createNewUser(String email, GoogleIdToken.Payload payload) {
        String fullName = (String) payload.get("name");

        User user = new User();
        user.setEmail(email);
        user.setName(fullName);
        user.setUsername(fullName);

        return userService.createUser(user);
    }

    private GoogleIdToken.Payload getGoogleTokenPayload(GoogleTokenResponse tokenResponse) {
        GoogleIdToken.Payload payload;

        try {
            payload = tokenResponse.parseIdToken().getPayload();
        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to connect to Google authentication server");
        }

        return payload;
    }

    @SneakyThrows
    private GoogleTokenResponse getGoogleTokenResponse(String code) {
        return new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                googleProperties.getClientId(),
                googleProperties.getSecret(),
                code,
                googleProperties.getRedirectUri()
        )
                .execute();
    }

    public String getUri() {
        GoogleAuthorizationCodeRequestUrl authorizationCodeRequestUrl = new GoogleAuthorizationCodeRequestUrl(
                googleProperties.getClientId(),
                googleProperties.getRedirectUri(),
                Arrays.asList(USER_INFO_EMAIL, USER_INFO_PROFILE)
        );
        return authorizationCodeRequestUrl
                .setState(GOOGLE_ACCOUNT_STATE)
                .build();
    }

}
