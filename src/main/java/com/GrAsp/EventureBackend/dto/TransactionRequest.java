package com.GrAsp.EventureBackend.dto;

import lombok.Data;

import java.util.Set;

@Data
public class TransactionRequest {
    private Integer ticketId;
    private Set<Integer> userDiscounts;
    private Set<Integer> eventDiscounts;
}
