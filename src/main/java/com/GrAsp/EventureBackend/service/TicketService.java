package com.GrAsp.EventureBackend.service;

import com.GrAsp.EventureBackend.dto.CreateTicketRequest;
import com.GrAsp.EventureBackend.model.Ticket;
import com.GrAsp.EventureBackend.repository.EventRepository;
import com.GrAsp.EventureBackend.repository.TicketRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log
@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(int id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> getTicketsByEventId(int eventId) {
        return ticketRepository.findTicketByEventId(eventId);
    }

    public Ticket addTicket(CreateTicketRequest req) {
        try {
            Ticket newTicket = req.toEntity();
            newTicket.setEvent(eventRepository.findById(req.getEventId()).orElse(null));
            return ticketRepository.save(newTicket);
        } catch (Exception e) {
            throw new RuntimeException("Can't save event, " + e.getMessage());
        }

    }

    public Ticket updateTicket(CreateTicketRequest req, Integer id) {
        try {
            Optional<Ticket> existingTicket = ticketRepository.findById(id);
            if (existingTicket.isPresent() && !existingTicket.get().getIsReleased()) {
                Ticket updatedTicket = existingTicket.get();
                updatedTicket.setName(req.getName());
                updatedTicket.setPrice(req.getPrice());
                updatedTicket.setAvailableSeat(req.getAvailableSeat());
                updatedTicket.setIsReleased(req.getIsReleased());
                updatedTicket.setIsClosed(req.getIsClosed());
//                log.info("Updated ticket: " + updatedTicket.getIsReleased().toString());
//                log.info("Requested ticket: " + req.isReleased());
                return ticketRepository.save(updatedTicket);
            } else {
                throw new RuntimeException("Ticket released or not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't save ticket, " + e.getMessage());
        }
    }

    public Ticket releaseTicket(int id) {
        try {
            Optional<Ticket> existingTicket = ticketRepository.findById(id);
            if (existingTicket.isPresent() && !existingTicket.get().getIsReleased()) {
                Ticket updatedTicket = existingTicket.get();
                updatedTicket.setIsReleased(true);
                return ticketRepository.save(updatedTicket);
            } else {
                throw new RuntimeException("Ticket released or not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't save ticket, " + e.getMessage());
        }

    }

    public Ticket closeTicket(int id) {
        try {
            Optional<Ticket> existingTicket = ticketRepository.findById(id);
            if (existingTicket.isPresent() && !existingTicket.get().getIsClosed()) {
                Ticket updatedTicket = existingTicket.get();
                updatedTicket.setIsClosed(true);
                return ticketRepository.save(updatedTicket);
            } else {
                throw new RuntimeException("Ticket closed or not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't save ticket, " + e.getMessage());
        }

    }

    public void deleteTicket(int id) {
        try {
            Optional<Ticket> ticket = ticketRepository.findById(id);
            if (ticket.isPresent() && !ticket.get().getIsReleased()) {
                ticketRepository.deleteById(id);
            } else {
                throw new RuntimeException("Ticket released");
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't delete ticket, " + e.getMessage());
        }

    }

}
