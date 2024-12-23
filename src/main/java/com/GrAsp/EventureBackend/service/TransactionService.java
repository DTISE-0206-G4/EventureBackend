package com.GrAsp.EventureBackend.service;

import com.GrAsp.EventureBackend.dto.TransactionRequest;
import com.GrAsp.EventureBackend.model.*;
import com.GrAsp.EventureBackend.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log
public class TransactionService {
    private final UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private TicketRepository ticketRepository;
    private EventDiscountRepository eventDiscountRepository;
    private UserDiscountRepository userDiscountRepository;

    public Optional<Transaction> getTransactionById(int id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getTransactionsByTicketId(int ticketId) {
        return transactionRepository.findTransactionsByTicketId(ticketId);
    }

    public List<Transaction> getTransactionsByUserId(int userId) {
        return transactionRepository.findTransactionsByUserId(userId);
    }

    public Page<Transaction> getTransactionsForUser(Pageable pageable, Integer userId) {
        return transactionRepository.findAllTransactionsWithUserId(userId, pageable);
    }

    public Page<Transaction> getTransactionsForOrganizer(Pageable pageable, Integer userId,Integer eventId) {
        return transactionRepository.findAllTransactionsWithOrganizerId(userId,eventId, pageable);
    }

    @Transactional
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

        if (!ticket.get().getIsReleased()){
            throw new RuntimeException("Ticket not released");
        }

        if (ticket.get().getIsClosed()){
            throw new RuntimeException("Ticket is closed");
        }

        //event discount
        Double totalPrice = ticket.get().getPrice();
        Optional<Transaction> existingTransaction = transactionRepository.findTransactionByTicketIdAndUserId(req.getTicketId(), userId);
        if (existingTransaction.isPresent()) {
            throw new RuntimeException("Ticket already bought by user");
        }

        existingTransaction = transactionRepository.findTransactionByUserIdAndTicketEvent_Id(userId, ticket.get().getEvent().getId());
        if (existingTransaction.isPresent()) {
            throw new RuntimeException("Ticket already bought for this event");
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

            if(eventDiscount.get().getExpiredAt().isBefore(OffsetDateTime.now())) {
                throw new RuntimeException("Event discount expired");
            }

            //todo +1 used eventDiscount
            EventDiscount updatedEventDiscount = eventDiscount.get();
            updatedEventDiscount.setUsed(eventDiscount.get().getUsed() + 1);
            eventDiscountRepository.save(updatedEventDiscount);
            //todo apply discount
            if (eventDiscount.get().getIsPercentage()) {
                totalPrice = totalPrice - (ticket.get().getPrice() * (eventDiscount.get().getAmount() / 100));
            } else {
                totalPrice = totalPrice - eventDiscount.get().getAmount();
            }
            if (totalPrice < 0) {
                totalPrice = 0.0;
            }
            transaction.getEventDiscounts().add(updatedEventDiscount);
        }

        //user discount
        for (Integer userDiscountId : req.getUserDiscounts()) {
            Optional<UserDiscount> userDiscount = userDiscountRepository.findById(userDiscountId);
            if (userDiscount.isEmpty()) {
                throw new RuntimeException("User discount not found");
            }
            if (userDiscount.get().getIsUsed()) {
                throw new RuntimeException("User discount is used");
            }
            if (userDiscount.get().getExpiredAt().isBefore(OffsetDateTime.now())) {
                log.info("expired: " + userDiscount.get().getExpiredAt());
                log.info("now: " + OffsetDateTime.now().toString());
                throw new RuntimeException("User discount expired");
            }

            //todo is_used=true userDiscount
            UserDiscount updatedUserDiscount = userDiscount.get();
            updatedUserDiscount.setIsUsed(true);
            userDiscountRepository.save(updatedUserDiscount);

            //todo apply discount
            if (userDiscount.get().getIsPercentage()) {
                totalPrice = totalPrice - (ticket.get().getPrice() * (userDiscount.get().getAmount() / 100));
            } else {
                totalPrice = totalPrice - userDiscount.get().getAmount();
            }
            transaction.getUserDiscounts().add(updatedUserDiscount);
        }

        //todo ticket sold++
        Ticket updatedTicket = ticket.get();
        updatedTicket.setSoldSeat(ticket.get().getSoldSeat() + 1);
        ticketRepository.save(updatedTicket);

        //todo save transaction

        transaction.setTicket(updatedTicket);
//        transaction.setTicketId(req.getTicketId());
        Optional<User> currentUser = userRepository.findById(userId);
        if (currentUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        transaction.setUser(currentUser.get());
        transaction.setTicketPrice(ticket.get().getPrice());
        transaction.setTotalPrice(totalPrice);
        return transactionRepository.save(transaction);
    }
}
