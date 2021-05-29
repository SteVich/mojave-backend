package com.mojave.payload.security;

import com.mojave.dictionary.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtAuthenticationResponse {

    String accessToken;
    String refreshToken;
    Long userId;
    Role role;

    public JwtAuthenticationResponse(String accessToken, String refreshToken, Long userId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
}
