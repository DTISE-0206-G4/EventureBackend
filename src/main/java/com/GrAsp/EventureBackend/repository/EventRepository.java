package com.GrAsp.EventureBackend.repository;

import com.GrAsp.EventureBackend.model.Event;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Optional<Event> findById(int id);

    List<Event> findByUserId(int id);

    @Query(value = "SELECT u FROM Event u WHERE u.title LIKE %:search%")
    Page<Event> findEventsWithSearch(@Param("search") String search, Pageable pageable);

    @Query(value = "SELECT u FROM Event u")
    Page<Event> findAllEvents(Pageable pageable);


    @Query(value = "SELECT u FROM Event u WHERE u.title LIKE %:search% AND u.user.id = :userId")
    Page<Event> findEventsWithSearchAndUserId(@Param("search") String search, @Param("userId") int userId, Pageable pageable);

    @Query(value = "SELECT u FROM Event u WHERE u.user.id = :userId")
    Page<Event> findAllEventsWithUserId(@Param("userId") int userId, Pageable pageable);

    @Query(value = "SELECT COUNT(u) FROM Event u WHERE u.user.id = :userId")
    long countEventsWithUserId(@Param("userId") int userId);


}
