package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.common.response.ApiResponse;
import com.GrAsp.EventureBackend.dto.CreateEventRequest;
import com.GrAsp.EventureBackend.model.Event;
import com.GrAsp.EventureBackend.security.config.Claims;
import com.GrAsp.EventureBackend.service.CategoryService;
import com.GrAsp.EventureBackend.service.EventService;
import com.GrAsp.EventureBackend.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {
    private final EventService eventService;
    private final UserService userService;
    private final CategoryService categoryService;

    public EventController(EventService eventService, UserService userService, CategoryService categoryService) {
        this.eventService = eventService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<?> getEvents() {
        return ApiResponse.successfulResponse("Events retrieved successfully", eventService.getAllEvents());
    }

    @GetMapping("/datatable")
    public ResponseEntity<?> getEventsDatatable(@RequestParam int draw,
                                                @RequestParam int start,
                                                @RequestParam int length,
                                                @RequestParam(required = false) String search,
                                                @RequestParam(required = false) String orderColumn,
                                                @RequestParam(required = false) String orderDir,
                                                @RequestParam(required = false) Integer userId) {
        int page = start / length;
        Sort.Direction direction = Sort.Direction.fromString(orderDir != null ? orderDir : "asc");
        Sort sort = Sort.by(direction, orderColumn != null ? orderColumn : "id");
        Pageable pageable = PageRequest.of(page, length, sort);
        Page<Event> eventPage = eventService.getEvents(pageable, search, userId);
        long totalRecords = eventService.count(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", totalRecords);
        response.put("recordsFiltered", eventPage.getTotalElements());
        response.put("data", eventPage.getContent());
        return ApiResponse.successfulResponse("Events retrieved successfully", response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER')")
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

    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable int id, @RequestBody CreateEventRequest event) {
        String email = Claims.getEmailFromJwt();
        var user = userService.getProfile(email);
        if (user == null) {
            return ApiResponse.failedResponse("User not found");
        }
        Event updatedEvent = eventService.updateEvent(event, id, user.getId());
        return ApiResponse.successfulResponse("Event updated successfully", updatedEvent);
    }

    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable int id) {
        eventService.deleteEvent(id);
        return ApiResponse.successfulResponse("Event deleted successfully");
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() { // Assuming this method is used to get categories
        return ApiResponse.successfulResponse("Categories retrieved successfully", categoryService.getAllCategories());
    }

}
