package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.common.response.ApiResponse;
import com.GrAsp.EventureBackend.dto.ChangePasswordRequest;
import com.GrAsp.EventureBackend.dto.LoginRequest;
import com.GrAsp.EventureBackend.dto.LogoutRequest;
import com.GrAsp.EventureBackend.dto.RegisterRequest;
import com.GrAsp.EventureBackend.security.config.Claims;
import com.GrAsp.EventureBackend.security.service.AuthService;
import com.GrAsp.EventureBackend.security.service.TokenService;
import com.GrAsp.EventureBackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final TokenService tokenService;

    public AuthController(AuthService authService, UserService userService, TokenService tokenService) {
        this.authService = authService;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest req) {
        return ApiResponse.successfulResponse("Login successful", authService.authenticateUser(req));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest req) {
        var result = userService.createUser(req);
        return ApiResponse.successfulResponse("User created successfully", result);
    }

    @PostMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest req) {
        String email = Claims.getEmailFromJwt();
        boolean isPasswordCorrect = authService.checkPassword(email, req.getOldPassword());
        if (!isPasswordCorrect) {
            return ApiResponse.failedResponse("Old password is incorrect");
        }
        boolean result = userService.changePassword(req, email);
        if (!result) {
            return ApiResponse.failedResponse("Failed to change password");
        }
        return ApiResponse.successfulResponse("Password changed successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Validated @RequestBody LogoutRequest req) {
        var accessToken = Claims.getJwtTokenString();
        req.setAccessToken(accessToken);
        return ApiResponse.successfulResponse("Logout successful", authService.logoutUser(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh() {
        String tokenType = Claims.getTokenTypeFromJwt();
        if (!"REFRESH".equals(tokenType)) {
            return ApiResponse.failedResponse("Invalid refresh token");
//            return Response.failedResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid token type for refresh");
        }
        String token = Claims.getJwtTokenString();
        return ApiResponse.successfulResponse("Refresh successful", tokenService.refreshAccessToken(token));
//        return Response.successfulResponse("Refresh successful", tokenRefreshUsecase.refreshAccessToken(token));
    }

}
