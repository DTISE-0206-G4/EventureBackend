package com.GrAsp.EventureBackend.security.service;

import com.GrAsp.EventureBackend.dto.LoginRequest;
import com.GrAsp.EventureBackend.dto.LoginResponse;
import com.GrAsp.EventureBackend.dto.LogoutRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final JwtDecoder jwtDecoder;
    private final BlacklistService blacklistService;
    private final long ACCESS_TOKEN_EXPIRY = 900L;
    private final long REFRESH_TOKEN_EXPIRY = 86400L;

    public AuthService(AuthenticationManager authenticationManager, TokenService tokenService, JwtDecoder jwtDecoder, BlacklistService blacklistService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.jwtDecoder = jwtDecoder;
        this.blacklistService = blacklistService;
    }

    public LoginResponse authenticateUser(LoginRequest req) {
        try {
//            log.info("Loggingin with");
//            log.info(req.getEmail());
//            log.info(req.getPassword());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
            String accessToken = tokenService.generateToken(authentication, TokenService.TokenType.ACCESS);
            String refreshToken = tokenService.generateToken(authentication, TokenService.TokenType.REFRESH);
            return new LoginResponse(accessToken, refreshToken, "Bearer");
        } catch (AuthenticationException e) {
//            log.info(e.getMessage());
            throw new RuntimeException("Wrong credentials");
        }
    }

    public boolean checkPassword(String email,String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            return authentication.isAuthenticated();
        } catch (AuthenticationException e) {
            throw new RuntimeException("Wrong password");
        }
    }

    public Boolean logoutUser(LogoutRequest req) {
        Jwt accessToken = jwtDecoder.decode(req.getAccessToken());
        Jwt refreshToken = jwtDecoder.decode(req.getRefreshToken());

        blacklistService.blacklistToken(accessToken.getTokenValue(), Objects.requireNonNull(accessToken.getExpiresAt()).toString());
        blacklistService.blacklistToken(refreshToken.getTokenValue(), Objects.requireNonNull(refreshToken.getExpiresAt()).toString());
        return Boolean.TRUE;
    }

}
