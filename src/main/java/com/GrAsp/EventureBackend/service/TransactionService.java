package com.GrAsp.EventureBackend.service;

import com.GrAsp.EventureBackend.dto.TransactionRequest;
import com.GrAsp.EventureBackend.model.Ticket;
import com.GrAsp.EventureBackend.model.Transaction;
import com.GrAsp.EventureBackend.repository.TicketRepository;
import com.GrAsp.EventureBackend.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionService {
    private TransactionRepository transactionRepository;
//    private TicketRepository ticketRepository;

    public Optional<Transaction> getTransactionById(int id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getTransactionsByTicketId(int ticketId) {
        return transactionRepository.findTransactionsByTicketId(ticketId);
    }

    public List<Transaction> getTransactionsByUserId(int userId) {
        return transactionRepository.findTransactionsByUserId(userId);
    }

    public Transaction saveTransaction(TransactionRequest req) {
        //todo get eventDiscount
        //todo check if eventDiscount is available
        //todo +1 used eventDiscount
        //todo get userDiscount
        //todo check if userDiscount is not used
        //todo isUsed=true userDiscount
        //todo save transaction
        return transactionRepository.save(req);
    }
}
