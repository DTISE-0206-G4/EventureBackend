package com.GrAsp.EventureBackend.repository;

import com.GrAsp.EventureBackend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Optional<Ticket> findById(int id);

    List<Ticket> findTicketByEventId(int id);

}
