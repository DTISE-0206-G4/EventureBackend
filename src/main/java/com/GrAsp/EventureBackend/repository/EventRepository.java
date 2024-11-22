package com.GrAsp.EventureBackend.repository;

import com.GrAsp.EventureBackend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Optional<Event> findById(int id);
//    Optional<Event> findByTitle(String title);
}
