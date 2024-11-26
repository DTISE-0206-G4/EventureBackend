package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.common.response.ApiResponse;
import com.GrAsp.EventureBackend.dto.CreateTicketRequest;
import com.GrAsp.EventureBackend.model.Ticket;
import com.GrAsp.EventureBackend.service.TicketService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/ticket")
@AllArgsConstructor
@Log
public class TicketController {
    private final TicketService ticketService;

    @GetMapping()
    public ResponseEntity<?> getTickets(@RequestParam Integer eventId) {
        return ApiResponse.successfulResponse("Tickets retrieved successfully", ticketService.getTicketsByEventId(eventId)); // Add this line
    }

    @PostMapping()
    public ResponseEntity<?> addTicket(@RequestBody CreateTicketRequest req) {
        return ApiResponse.successfulResponse("Ticket added successfully", ticketService.addTicket(req)); // Add this line
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicket(@PathVariable Integer id) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);
        if (ticket.isPresent()) {
            return ApiResponse.successfulResponse("Ticket retrieved successfully", ticket);
        }
        return ApiResponse.failedResponse("Ticket not found");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTicket(@RequestBody CreateTicketRequest req, @PathVariable int id) {
        log.info("Updating ticket name: " + req.getName());
        log.info("Updating ticket release: " + req.getIsReleased());
        return ApiResponse.successfulResponse("Ticket updated successfully", ticketService.updateTicket(req, id));
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<?> releaseTicket(@PathVariable int id) {
        return ApiResponse.successfulResponse("Ticket released successfully", ticketService.releaseTicket(id));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<?> closeTicket(@PathVariable int id) {
        return ApiResponse.successfulResponse("Ticket closed successfully", ticketService.closeTicket(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable int id) {
        ticketService.deleteTicket(id);
        return ApiResponse.successfulResponse("Ticket deleted successfully");
    }
}
