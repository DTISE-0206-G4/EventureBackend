package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.common.response.ApiResponse;
import com.GrAsp.EventureBackend.dto.EditUserProfileRequest;
import com.GrAsp.EventureBackend.model.UserDiscount;
import com.GrAsp.EventureBackend.repository.UserDiscountRepository;
import com.GrAsp.EventureBackend.security.config.Claims;
import com.GrAsp.EventureBackend.service.UserDiscountService;
import com.GrAsp.EventureBackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserDiscountService userDiscountService;

    @GetMapping()
    public ResponseEntity<?> getUser() {
        String email = Claims.getEmailFromJwt();
        var user = userService.getProfile(email);
        if (user == null) {
            return ApiResponse.failedResponse("User not found");
        }
        return ApiResponse.successfulResponse("User found", user);
    }

    @PostMapping()
    public ResponseEntity<?> editUserProfile(@RequestBody EditUserProfileRequest req) {
        String email = Claims.getEmailFromJwt();
        var user = userService.getProfile(email);
        if (user == null) {
            return ApiResponse.failedResponse("User not found");
        }
        userService.setProfile(req, email);
        return ApiResponse.successfulResponse("Profile changed");
    }

    @GetMapping("/discount")
    public ResponseEntity<?> getUserDiscount() {
        String email = Claims.getEmailFromJwt();
        var user = userService.getProfile(email);
        if (user == null) {
            return ApiResponse.failedResponse("User not found");
        }
        List<UserDiscount> userDiscounts = userDiscountService.getUserDiscounts(user.getId());
        return ApiResponse.successfulResponse("User discounts", userDiscounts);
    }
}
