package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.common.response.ApiResponse;

import com.GrAsp.EventureBackend.repository.TransactionRepository;
import com.GrAsp.EventureBackend.security.config.Claims;
import com.GrAsp.EventureBackend.service.AnalyticService;
import com.GrAsp.EventureBackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/analytic")

public class AnalyticController {

    private final AnalyticService analyticService;
    private final UserService userService;

    public AnalyticController(AnalyticService analyticService, UserService userService, TransactionRepository transactionRepository) {
        this.analyticService = analyticService;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<?> getAnalytics(@RequestParam(required = true) Integer range) {
        String email = Claims.getEmailFromJwt();
        var user = userService.getProfile(email);
        if (user == null) {
            return ApiResponse.failedResponse("User not found");
        }

        return ApiResponse.successfulResponse("Analytics retrieved successfully", analyticService.calculateAnalytics(range, user.getId()));
    }
}
