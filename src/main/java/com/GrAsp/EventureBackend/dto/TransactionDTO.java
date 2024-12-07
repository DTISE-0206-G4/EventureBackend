package com.GrAsp.EventureBackend.dto;

import com.GrAsp.EventureBackend.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Integer id;
    private Integer userId;
    private Integer ticketId;
    private Double ticketPrice;
    private Double totalPrice;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.userId = transaction.getUserId();
        this.ticketId = transaction.getTicket().getId();  // Foreign key reference to Event
        this.ticketPrice = transaction.getTicketPrice();
        this.totalPrice = transaction.getTotalPrice();
        this.createdAt = transaction.getCreatedAt();
        this.updatedAt = transaction.getUpdatedAt();
        this.deletedAt = transaction.getDeletedAt();
    }
}
