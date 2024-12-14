package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.common.response.ApiResponse;
import com.GrAsp.EventureBackend.dto.TransactionRequest;
import com.GrAsp.EventureBackend.model.Event;
import com.GrAsp.EventureBackend.model.Role;
import com.GrAsp.EventureBackend.model.Transaction;
import com.GrAsp.EventureBackend.security.config.Claims;
import com.GrAsp.EventureBackend.service.TransactionService;
import com.GrAsp.EventureBackend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/transaction")
@Log
public class TransactionController {
    private TransactionService transactionService;
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getTransactions(@RequestParam Integer ticketId) {
        return ApiResponse.successfulResponse("Transactions retrieved successfully", transactionService.getTransactionsByTicketId(ticketId)); // Add this line
    }

    @GetMapping("/event")
    public ResponseEntity<?> getTransactionsForOrganizer(@RequestParam Integer eventId, @RequestParam int start, @RequestParam int length) {
        String email = Claims.getEmailFromJwt();
        var user = userService.getProfile(email);
        if (user == null) {
            return ApiResponse.failedResponse("User not found");
        }
        int page = start / length;
        Sort.Direction direction = Sort.Direction.fromString("desc");
        Sort sort = Sort.by(direction, "id");
        Pageable pageable = PageRequest.of(page, length, sort);
        Page<Transaction> transactionDatatable = transactionService.getTransactionsForOrganizer(pageable, user.getId(), eventId);
        Map<String, Object> response = new HashMap<>();
        response.put("recordsFiltered", transactionDatatable.getTotalElements());
        response.put("data", transactionDatatable.getContent());

        return ApiResponse.successfulResponse("Transactions retrieved successfully", response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable Integer id) {
        String email = Claims.getEmailFromJwt();
        var user = userService.getProfile(email);
        if (user == null) {
            return ApiResponse.failedResponse("User not found");
        }
        Optional<Transaction> transaction = transactionService.getTransactionById(id);
        if (transaction.isEmpty()) {
            return ApiResponse.failedResponse("Transaction not found");
        }
        for (Role role : user.getRoles()) {
            if ("ATTENDEE".equals(role.getName())) {
                if (!Objects.equals(transaction.get().getUser().getId(), user.getId())) {
                    return ApiResponse.failedResponse("Transaction not found");
                }
            } else if ("ORGANIZER".equals(role.getName())) {
                if (!Objects.equals(transaction.get().getTicket().getEvent().getUser().getId(), user.getId())) {
                    return ApiResponse.failedResponse("Transaction not found");
                }
            }
        }
        return ApiResponse.successfulResponse("Transaction retrieved successfully", transaction.get()); // Add this line
    }

    @PreAuthorize("hasAuthority('SCOPE_ATTENDEE')")
    @GetMapping("/datatable")
    public ResponseEntity<?> getTransactionsForUser(@RequestParam int start,
                                                    @RequestParam int length) {
        String email = Claims.getEmailFromJwt();
        var user = userService.getProfile(email);
        if (user == null) {
            return ApiResponse.failedResponse("User not found");
        }
        int page = start / length;
        Sort.Direction direction = Sort.Direction.fromString("desc");
        Sort sort = Sort.by(direction, "id");
        Pageable pageable = PageRequest.of(page, length, sort);
        Page<Transaction> transactionDatatable = transactionService.getTransactionsForUser(pageable, user.getId());
//        long totalRecords = transactionService.count(userId);
        Map<String, Object> response = new HashMap<>();
//        response.put("draw", draw);
//        response.put("recordsTotal", totalRecords);
        response.put("recordsFiltered", transactionDatatable.getTotalElements());
        response.put("data", transactionDatatable.getContent());

        return ApiResponse.successfulResponse("Transactions retrieved successfully", response); // Add this line
    }

    @PreAuthorize("hasAuthority('SCOPE_ATTENDEE')")
    @PostMapping()
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        String email = Claims.getEmailFromJwt();
        var user = userService.getProfile(email);
        if (user == null) {
            return ApiResponse.failedResponse("User not found");
        }

        log.info(transactionRequest.getEventDiscounts().toString());
        return ApiResponse.successfulResponse("Transaction created successfully", transactionService.saveTransaction(transactionRequest, user.getId())); // Add this line
    }

}
