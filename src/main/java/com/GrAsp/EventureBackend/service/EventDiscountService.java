package com.GrAsp.EventureBackend.service;

import com.GrAsp.EventureBackend.dto.CreateEventDiscountRequest;
import com.GrAsp.EventureBackend.model.Event;
import com.GrAsp.EventureBackend.model.EventDiscount;
import com.GrAsp.EventureBackend.repository.EventDiscountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventDiscountService {
    public final EventDiscountRepository eventDiscountRepository;

    public List<EventDiscount> getAllEventDiscounts(@RequestParam Integer eventId) {
        if (eventId != null) {
            return eventDiscountRepository.findEventDiscountsByEventId(eventId);
        } else {
            return eventDiscountRepository.findAll();
        }
    }

    public Optional<EventDiscount> getEventDiscountByCode(@RequestParam String code) {
        return eventDiscountRepository.findEventDiscountByCode(code);
    }

    public Optional<EventDiscount> getEventDiscountById(@RequestParam Integer id) {
        return eventDiscountRepository.findById(id);
    }

    public EventDiscount addEventDiscount(CreateEventDiscountRequest req) { // Complete this method
        try {
            return eventDiscountRepository.save(req.toEntity());
        } catch (Exception e) {
            throw new RuntimeException("Can't save event discount, " + e.getMessage());
        }
    }

    public EventDiscount updateEventDiscount(CreateEventDiscountRequest req, Integer id) {
        try {
            Optional<EventDiscount> existingEvent = eventDiscountRepository.findById(id);
            if (existingEvent.isPresent()) {
                EventDiscount updatedEvent = existingEvent.get();
                updatedEvent.setTitle(req.getTitle());
                updatedEvent.setDescription(req.getDescription());
                updatedEvent.setCode(req.getCode());
                updatedEvent.setAmount(req.getAmount());
                updatedEvent.setIsPercentage(req.getIsPercentage());
                updatedEvent.setAvailable(req.getAvailable());
                updatedEvent.setIsReleased(req.getIsReleased());
                updatedEvent.setIsClosed(req.getIsClosed());
                return eventDiscountRepository.save(updatedEvent);
            } else {
                throw new RuntimeException("Can't find event discount with id " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't save event discount, " + e.getMessage());
        }
    }

    public void deleteEventDiscount(Integer id) {
        try {
            Optional<EventDiscount> eventDiscount = eventDiscountRepository.findById(id);
            if (eventDiscount.isPresent()) {
                EventDiscount deletedEvent = eventDiscount.get();
                deletedEvent.setDeletedAt(OffsetDateTime.now());
                eventDiscountRepository.save(deletedEvent);
            } else {
                throw new RuntimeException("Event discount not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't delete event discount, " + e.getMessage());
        }

    }
}
