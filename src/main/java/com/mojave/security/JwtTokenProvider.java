package com.mojave.security;

import com.mojave.config.PropertiesConfig;
import com.mojave.dictionary.Role;
import com.mojave.exception.InvalidTokenException;
import com.mojave.payload.security.RoleResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtTokenProvider {

    static final String ROLE_CLAIM = "roles";
    PropertiesConfig.JwtProperties properties;

    public String generateToken(Authentication authentication, Long expiration, List<RoleResponse> roles) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .claim(ROLE_CLAIM, roles)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, properties.getSecretKey())
                .compact();
    }

    public String generateTokenFromId(Long id, Long expiration, List<RoleResponse> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(Long.toString(id))
                .setIssuedAt(new Date())
                .claim(ROLE_CLAIM, roles)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, properties.getSecretKey())
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(properties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public Long getExpirationFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(properties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().getTime();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(properties.getSecretKey()).parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Expired or invalid JWT token");
        }
    }

    public Boolean checkExpirationAccessToken(String refreshToken){
        Long expirationRefreshToken = getExpirationFromJWT(refreshToken);

        return expirationRefreshToken - getConstantExpirationRefreshToken() < 0;
    }

    public Long getConstantExpirationRefreshToken(){
        return new Date().getTime() + properties.getExpirationRefreshToken();
    }

}
