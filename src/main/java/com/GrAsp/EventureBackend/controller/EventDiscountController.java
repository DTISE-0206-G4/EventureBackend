package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.common.response.ApiResponse;
import com.GrAsp.EventureBackend.dto.CreateEventDiscountRequest;
import com.GrAsp.EventureBackend.model.EventDiscount;
import com.GrAsp.EventureBackend.service.EventDiscountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/event_discount")
@AllArgsConstructor
public class EventDiscountController {
    private final EventDiscountService eventDiscountService;

    @GetMapping()
    public ResponseEntity<?> getEventDiscounts(@RequestParam(required = false) Integer eventId) {
        return ApiResponse.successfulResponse("Event discounts retrieved successfully", eventDiscountService.getAllEventDiscounts(eventId));
    }

    @PostMapping()
    public ResponseEntity<?> addEventDiscount(@RequestBody CreateEventDiscountRequest req) {
        return ApiResponse.successfulResponse("Event discount added successfully", eventDiscountService.addEventDiscount(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventDiscountById(@PathVariable Integer id) {
        return ApiResponse.successfulResponse("Event discount retrieved successfully", eventDiscountService.getEventDiscountById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEventDiscount(@PathVariable Integer id, @RequestBody CreateEventDiscountRequest req) {
        return ApiResponse.successfulResponse("Event discount updated successfully", eventDiscountService.updateEventDiscount(req, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEventDiscount(@PathVariable Integer id) {
        eventDiscountService.deleteEventDiscount(id);
        return ApiResponse.successfulResponse("Event discount deleted successfully");
    }

}
