package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.common.response.ApiResponse;
import com.GrAsp.EventureBackend.dto.ReviewRequest;
import com.GrAsp.EventureBackend.security.config.Claims;
import com.GrAsp.EventureBackend.service.ReviewService;
import com.GrAsp.EventureBackend.service.UserService;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
    public final ReviewService reviewService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<?> getReviewsByEventId(@RequestParam Integer eventId) {
        return ApiResponse.successfulResponse("Reviews retrieved successfully", reviewService.getAllReviewsByEventId(eventId));
    }

    @PreAuthorize("hasAuthority('SCOPE_ATTENDEE')")
    @GetMapping("/user")
    public ResponseEntity<?> getReviewForUser(@RequestParam Integer eventId) {
        String email = Claims.getEmailFromJwt();
        var user = userService.getProfile(email);
        if (user == null) {
            return ApiResponse.failedResponse("User not found");
        }
        return ApiResponse.successfulResponse("Reviews retrieved successfully", reviewService.getReviewByUserIdAndEventId(user.getId(), eventId));
    }

    @GetMapping("/average_stars")
    public ResponseEntity<?> getAverageStars() {
        String email = Claims.getEmailFromJwt();
        var user = userService.getProfile(email);
        if (user == null) {
            return ApiResponse.failedResponse("User not found");
        }
        return ApiResponse.successfulResponse("Average stars retrieved successfully", reviewService.getAverageStartByOrganizer(user.getId()));
    }

    @PreAuthorize("hasAuthority('SCOPE_ATTENDEE')")
    @PostMapping()
    public ResponseEntity<?> addReview(@RequestBody ReviewRequest req) {
        String email = Claims.getEmailFromJwt();
        var user = userService.getProfile(email);
        if (user == null) {
            return ApiResponse.failedResponse("User not found");
        }
        return ApiResponse.successfulResponse("Review added successfully", reviewService.addReview(req, user.getId()));
    }
}
