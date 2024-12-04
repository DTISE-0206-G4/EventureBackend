package com.GrAsp.EventureBackend.controller;

import com.GrAsp.EventureBackend.common.response.ApiResponse;
import com.GrAsp.EventureBackend.dto.TransactionRequest;
import com.GrAsp.EventureBackend.security.config.Claims;
import com.GrAsp.EventureBackend.service.TransactionService;
import com.GrAsp.EventureBackend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
