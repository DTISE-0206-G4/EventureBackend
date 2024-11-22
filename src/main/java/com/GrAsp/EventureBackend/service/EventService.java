package com.GrAsp.EventureBackend.service;

import com.GrAsp.EventureBackend.dto.AddEventRequest;
import com.GrAsp.EventureBackend.model.Event;
import com.GrAsp.EventureBackend.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log
public class EventService {
    public final EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Integer id) {
        return eventRepository.findById(id);
    }

    public Event addEvent(AddEventRequest event, Integer userId) {
        try {
            Event newEvent = new Event();
            newEvent.setTitle(event.getTitle());
            newEvent.setDescription(event.getDescription());
            newEvent.setStartDate(event.getStartDate());
            newEvent.setEndDate(event.getEndDate());
            newEvent.setLocation(event.getLocation());
            newEvent.setCategories(event.getCategories());
            newEvent.setUserId(userId);
            return eventRepository.save(newEvent);

        } catch (Exception e) {
            throw new RuntimeException("Can't save event, " + e.getMessage());
        }

    }
}
