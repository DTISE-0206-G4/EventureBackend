package com.GrAsp.EventureBackend.security.service;

import com.GrAsp.EventureBackend.dto.LoginRequest;
import com.GrAsp.EventureBackend.dto.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public LoginResponse authenticateUser(LoginRequest req) {
        try {
//            log.info("Loggingin with");
//            log.info(req.getEmail());
//            log.info(req.getPassword());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
            String token = tokenService.generateToken(authentication);
            return new LoginResponse(token);
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
}
