package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.common.response.ApiResponse;
import com.GrAsp.EventureBackend.dto.CreateTicketRequest;
import com.GrAsp.EventureBackend.dto.TicketDTO;
import com.GrAsp.EventureBackend.model.Ticket;
import com.GrAsp.EventureBackend.service.TicketService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        Optional<Ticket> ticket = ticketService.getTicketById(eventId);
        if (ticket.isEmpty()) {
           return ApiResponse.failedResponse("Ticket not found");
        }
        TicketDTO ticketDTO = new TicketDTO(ticket.get());
        return ApiResponse.successfulResponse("Tickets retrieved successfully", ticketDTO); // Add this line
    }

    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER')")
    @PostMapping()
    public ResponseEntity<?> addTicket(@RequestBody CreateTicketRequest req) {
        Ticket ticket=ticketService.addTicket(req);
        TicketDTO ticketDTO = new TicketDTO(ticket);
        return ApiResponse.successfulResponse("Ticket added successfully", ticketDTO); // Add this line
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicket(@PathVariable Integer id) {
        Optional<Ticket> ticket = ticketService.getTicketById(id);
        if (ticket.isEmpty()) {
            return ApiResponse.failedResponse("Ticket not found");
        }
        TicketDTO ticketDTO = new TicketDTO(ticket.get());
        return ApiResponse.successfulResponse("Ticket retrieved successfully", ticketDTO);

    }

    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTicket(@RequestBody CreateTicketRequest req, @PathVariable int id) {
        log.info("Updating ticket name: " + req.getName());
        log.info("Updating ticket release: " + req.getIsReleased());
        Ticket ticket= ticketService.updateTicket(req, id);
        TicketDTO ticketDTO = new TicketDTO(ticket);
        return ApiResponse.successfulResponse("Ticket updated successfully", ticketDTO);
    }

    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER')")
    @PostMapping("/{id}/release")
    public ResponseEntity<?> releaseTicket(@PathVariable int id) {
        Ticket ticket = ticketService.releaseTicket(id);
        TicketDTO ticketDTO = new TicketDTO(ticket);
        return ApiResponse.successfulResponse("Ticket released successfully", ticketDTO);
    }

    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER')")
    @PostMapping("/{id}/close")
    public ResponseEntity<?> closeTicket(@PathVariable int id) {
        Ticket ticket = ticketService.closeTicket(id);
        TicketDTO ticketDTO = new TicketDTO(ticket);
        return ApiResponse.successfulResponse("Ticket closed successfully", ticketService.closeTicket(id));
    }

    @PreAuthorize("hasAuthority('SCOPE_ORGANIZER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable int id) {
        ticketService.deleteTicket(id);
        return ApiResponse.successfulResponse("Ticket deleted successfully");
    }
}
