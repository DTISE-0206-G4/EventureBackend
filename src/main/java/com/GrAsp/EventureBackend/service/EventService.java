package com.GrAsp.EventureBackend.service;

import com.GrAsp.EventureBackend.dto.CreateEventRequest;
import com.GrAsp.EventureBackend.model.Category;
import com.GrAsp.EventureBackend.model.Event;
import com.GrAsp.EventureBackend.repository.CategoryRepository;
import com.GrAsp.EventureBackend.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log
public class EventService {
    public final EventRepository eventRepository;
    public final CategoryRepository categoryRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Page<Event> getEvents(Pageable pageable, String search) {
        if (search != null && !search.isEmpty()) {
            return eventRepository.findEventsWithSearch(search, pageable);
        } else {
            return eventRepository.findAllEvents(pageable);
        }
    }

    public long count() {
        return eventRepository.count();
    }

    public long countFiltered(String search) {
        if (search != null && !search.isEmpty()) {
            return eventRepository.countEventsWithSearch(search);
        } else {
            return eventRepository.count();
        }
    }

    public List<Event> getEventsByUserId(int id) {
        return eventRepository.findByUserId(id);
    }

    public Optional<Event> getEventById(Integer id) {
        return eventRepository.findById(id);
    }

    public Event addEvent(CreateEventRequest event, Integer userId) {
        try {
            Event newEvent = event.toEntity();
            newEvent.setUserId(userId);
            if (!event.getCategories().isEmpty()) {
                for (Integer c : event.getCategories()) {
                    Optional<Category> category = categoryRepository.findById(c);
                    if (category.isPresent()) {
                        newEvent.getCategories().add(category.get());
                    } else {
                        throw new RuntimeException("Event category not found");
                    }
                }
            }
            return eventRepository.save(newEvent);

        } catch (Exception e) {
            throw new RuntimeException("Can't save event, " + e.getMessage());
        }
    }

    public Event updateEvent(CreateEventRequest event, Integer id) {
        try {
            Optional<Event> existingEvent = eventRepository.findById(id);
            if (existingEvent.isPresent()) {
                Event updatedEvent = existingEvent.get();
                updatedEvent.setTitle(event.getTitle());
                updatedEvent.setDescription(event.getDescription());
                updatedEvent.setLocation(event.getLocation());
                DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                OffsetDateTime startTime = OffsetDateTime.parse(event.getStartTime(), formatter);
                OffsetDateTime endTime = OffsetDateTime.parse(event.getEndTime(), formatter);
                updatedEvent.setStartTime(startTime);
                updatedEvent.setEndTime(endTime);
                if (updatedEvent.getCategories().isEmpty() && !event.getCategories().isEmpty()) {
                    for (Integer c : event.getCategories()) {
                        Optional<Category> category = categoryRepository.findById(c);
                        if (category.isPresent()) {
                            updatedEvent.getCategories().add(category.get());
                        } else {
                            throw new RuntimeException("Event category not found");
                        }
                    }
                } else if (!updatedEvent.getCategories().isEmpty() && event.getCategories().isEmpty()) {
                    updatedEvent.setCategories(null);//set empty categories
                } else if (!updatedEvent.getCategories().isEmpty()) {
                    updatedEvent.getCategories().clear();
                    for (Integer c : event.getCategories()) {
                        Optional<Category> category = categoryRepository.findById(c);
                        if (category.isPresent()) {
                            updatedEvent.getCategories().add(category.get());
                        } else {
                            throw new RuntimeException("Event category not found");
                        }
                    }

                }
                return eventRepository.save(updatedEvent);
            } else {
                throw new RuntimeException("Event not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't update event, " + e.getMessage());
        }
    }

    public void deleteEvent(Integer id) {
        try {
            Optional<Event> event = eventRepository.findById(id);
            if (event.isPresent()) {
                Event deletedEvent = event.get();
                deletedEvent.setDeletedAt(OffsetDateTime.now());
                eventRepository.save(deletedEvent);
            } else {
                throw new RuntimeException("Event not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't delete event, " + e.getMessage());
        }

    }
}