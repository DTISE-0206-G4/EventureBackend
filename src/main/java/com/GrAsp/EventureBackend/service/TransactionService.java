package com.GrAsp.EventureBackend.service;

import com.GrAsp.EventureBackend.dto.TransactionRequest;
import com.GrAsp.EventureBackend.model.EventDiscount;
import com.GrAsp.EventureBackend.model.Ticket;
import com.GrAsp.EventureBackend.model.Transaction;
import com.GrAsp.EventureBackend.repository.EventDiscountRepository;
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
    private TicketRepository ticketRepository;
    private EventDiscountRepository eventDiscountRepository;

    public Optional<Transaction> getTransactionById(int id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getTransactionsByTicketId(int ticketId) {
        return transactionRepository.findTransactionsByTicketId(ticketId);
    }

    public List<Transaction> getTransactionsByUserId(int userId) {
        return transactionRepository.findTransactionsByUserId(userId);
    }

    public Transaction saveTransaction(TransactionRequest req, Integer userId) {
        Transaction transaction = new Transaction();
        //todo get ticket price
        //todo check if user already bought ticket
        //todo get eventDiscount
        //todo check if eventDiscount is available
        //todo +1 used eventDiscount
        //todo get userDiscount
        //todo check if userDiscount is not used
        //todo isUsed=true userDiscount

        //todo check ticket availability
        Optional<Ticket> ticket = ticketRepository.findById(req.getTicketId());
        if (ticket.isEmpty()) {
            throw new RuntimeException("Ticket not found");
        }
        if (ticket.get().getSoldSeat() >= ticket.get().getAvailableSeat()) {
            throw new RuntimeException("Ticket sold out");
        }

        //event discount
        Double totalPrice = ticket.get().getPrice();
        Optional<Transaction> existingTransaction = transactionRepository.findTransactionByTicketIdAndUserId(req.getTicketId(), userId);
        if (existingTransaction.isPresent()) {
            throw new RuntimeException("Ticket already bought by user");
        }
        for (Integer eventDiscountId : req.getEventDiscounts()) {
            Optional<EventDiscount> eventDiscount = eventDiscountRepository.findById(eventDiscountId);
            if (eventDiscount.isEmpty()) {
                throw new RuntimeException("Event discount not found");
            }
            //todo check if discount released and closed
            if (!eventDiscount.get().getIsReleased()) {
                throw new RuntimeException("Event discount not released");
            }
            if (eventDiscount.get().getIsClosed()) {
                throw new RuntimeException("Event discount is closed");
            }
            //todo check if discount available
            if (eventDiscount.get().getUsed() >= eventDiscount.get().getAvailable()) {
                throw new RuntimeException("Event discount is used up");
            }

            //todo +1 used eventDiscount
            EventDiscount updatedEventDiscount = eventDiscount.get();
            updatedEventDiscount.setUsed(eventDiscount.get().getUsed() + 1);
            eventDiscountRepository.save(updatedEventDiscount);
            //todo apply discount
            if (eventDiscount.get().getIsPercentage()) {
                totalPrice = totalPrice * (1 - (eventDiscount.get().getAmount() / 100));
            } else {
                totalPrice = totalPrice - eventDiscount.get().getAmount();
            }
            if (totalPrice < 0) {
                totalPrice = 0.0;
            }
            transaction.getEventDiscounts().add(updatedEventDiscount);
        }

        //todo ticket sold++
        Ticket updatedTicket = ticket.get();
        updatedTicket.setSoldSeat(ticket.get().getSoldSeat() + 1);
        ticketRepository.save(updatedTicket);

        //todo save transaction

        transaction.setTicketId(req.getTicketId());
        transaction.setUserId(userId);
        transaction.setTicketPrice(ticket.get().getPrice());
        transaction.setTotalPrice(totalPrice);
        return transactionRepository.save(transaction);
    }
}
