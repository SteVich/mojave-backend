package com.mojave.security.google;

import com.mojave.config.ApiHostConfig;
import com.mojave.payload.JwtAuthenticationResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoogleLoginController {

    GoogleLoginService googleLoginService;
    ApiHostConfig hostConfig;

    @GetMapping("api/google/login-with-google")
    public void sendGoogleRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect(googleLoginService.getUri());
    }

    @GetMapping("google/oauth2callback")
    public void getCVGoogleCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doSuccessfulLoginRedirect(request, response, hostConfig.client);
    }

    private void doSuccessfulLoginRedirect(HttpServletRequest request,
                                           HttpServletResponse response,
                                           String host) throws IOException {
        StringBuffer fullUrlBuffer = request.getRequestURL();
        if (request.getQueryString() != null) {
            fullUrlBuffer.append('?').append(request.getQueryString());
        }

        JwtAuthenticationResponse authResponse = googleLoginService.findAndAuthenticateUser(fullUrlBuffer);

        response.sendRedirect(host +
                "/auth?accessToken=" + authResponse.getAccessToken() +
                "&refreshToken=" + authResponse.getRefreshToken() +
                "&userId=" + authResponse.getUserId());
    }

}
