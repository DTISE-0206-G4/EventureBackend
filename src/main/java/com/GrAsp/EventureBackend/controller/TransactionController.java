package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.common.response.ApiResponse;
import com.GrAsp.EventureBackend.dto.TransactionRequest;
import com.GrAsp.EventureBackend.model.Event;
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

    @PreAuthorize("hasAuthority('SCOPE_ATTENDEE')")
    @GetMapping("/user")
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
        return ApiResponse.successfulResponse("Transaction created successfully", transactionService.saveTransaction(transactionRequest,user.getId())); // Add this line
    }

}
