package com.GrAsp.EventureBackend.repository;

import com.GrAsp.EventureBackend.model.Transaction;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Optional<Transaction> findById(int id);

    List<Transaction> findTransactionsByTicketId(int ticketId);

    List<Transaction> findTransactionsByUserId(int userId);

    Optional<Transaction> findTransactionByTicketIdAndUserId(int ticketId, int userId);
}
