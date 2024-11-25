package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.common.response.ApiResponse;
import com.GrAsp.EventureBackend.dto.CreateEventRequest;
import com.GrAsp.EventureBackend.model.Event;
import com.GrAsp.EventureBackend.security.config.Claims;
import com.GrAsp.EventureBackend.service.EventService;
import com.GrAsp.EventureBackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/my_event")
    public ResponseEntity<?> getEventsForOrganizer() {
        String email = Claims.getEmailFromJwt();
        var user = userService.getProfile(email);
        if (user == null) {
            return ApiResponse.failedResponse("User not found");
        }
        return ApiResponse.successfulResponse("Events retrieved successfully", eventService.getEventsByUserId(user.getId()));
    }

    @PostMapping()
    public ResponseEntity<?> addEvent(@RequestBody CreateEventRequest event) {
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable int id) {
        Optional<Event> event = eventService.getEventById(id);
        if (event.isPresent()) {
            return ApiResponse.successfulResponse("Event retrieved successfully", event);
        }
        return ApiResponse.failedResponse("Event not found");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable int id, @RequestBody CreateEventRequest event) {
        Event updatedEvent= eventService.updateEvent(event,id);
        return ApiResponse.successfulResponse("Event updated successfully", updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable int id) {
        eventService.deleteEvent(id);
        return ApiResponse.successfulResponse("Event deleted successfully");
    }
}
