package com.GrAsp.EventureBackend.repository;

import com.GrAsp.EventureBackend.model.EventDiscount;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface EventDiscountRepository extends JpaRepository<EventDiscount, Integer> {
    List<EventDiscount> findEventDiscountsByEventId(int eventId);

    Optional<EventDiscount> findEventDiscountByCode(@Size(max = 255) @NotNull String code);

    Optional<EventDiscount> findById(int id);

    List<EventDiscount> findEventDiscountsByEventIdAndExpiredAtAfterAndIsReleasedAndIsClosed(Integer eventId, OffsetDateTime now, Boolean isReleased,Boolean isClosed);
}
