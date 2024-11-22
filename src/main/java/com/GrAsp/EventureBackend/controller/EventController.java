package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.common.response.ApiResponse;
import com.GrAsp.EventureBackend.dto.AddEventRequest;
import com.GrAsp.EventureBackend.model.Event;
import com.GrAsp.EventureBackend.model.UserDiscount;
import com.GrAsp.EventureBackend.repository.EventRepository;
import com.GrAsp.EventureBackend.security.config.Claims;
import com.GrAsp.EventureBackend.service.EventService;
import com.GrAsp.EventureBackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {
    private final EventService eventService;
    private final UserService userService;

    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<?> getEvents() {
        return ApiResponse.successfulResponse("Events retrieved successfully", eventService.getAllEvents());
    }

    @PostMapping()
    public ResponseEntity<?> addEvent(@RequestBody AddEventRequest event) {
        String email = Claims.getEmailFromJwt();
        var user = userService.getProfile(email);
        if (user == null) {
            return ApiResponse.failedResponse("User not found");
        }
        var result = eventService.addEvent(event, user.getId());
        if (result == null) {
            return ApiResponse.failedResponse("Event not added");
        }
        return ApiResponse.successfulResponse("Event added successfully", result);
    }
//        List<UserDiscount> userDiscounts = userDiscountService.getUserDiscounts(user.getId());

//        return ApiResponse.successfulResponse("");
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable int id) {
        Optional<Event> event = eventService.getEventById(id);
        if (event.isPresent()) {
            return ApiResponse.successfulResponse("Event retrieved successfully", event);
        }
        return ApiResponse.failedResponse("Event not found");
    }
}
