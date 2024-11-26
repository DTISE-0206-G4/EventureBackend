package com.GrAsp.EventureBackend.service;

import com.GrAsp.EventureBackend.dto.CreateEventDiscountRequest;
import com.GrAsp.EventureBackend.model.EventDiscount;
import com.GrAsp.EventureBackend.repository.EventDiscountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
}
