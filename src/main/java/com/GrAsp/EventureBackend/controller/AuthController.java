package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.dto.LoginRequest;
import com.GrAsp.EventureBackend.dto.RegisterRequest;
import com.GrAsp.EventureBackend.security.service.AuthService;
import com.GrAsp.EventureBackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest req) {
//        return ApiResponse.successfulResponse("Login successful", loginUsecase.authenticateUser(req));

        return ResponseEntity.ok(authService.authenticateUser(req));
    }
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest req) {
        var result = userService.createUser(req);
        return ResponseEntity.ok(result);
    }

}
