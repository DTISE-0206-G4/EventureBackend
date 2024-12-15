package com.GrAsp.EventureBackend.service;

import com.GrAsp.EventureBackend.dto.CreateEventRequest;
import com.GrAsp.EventureBackend.model.Category;
import com.GrAsp.EventureBackend.model.Event;
import com.GrAsp.EventureBackend.model.User;
import com.GrAsp.EventureBackend.repository.CategoryRepository;
import com.GrAsp.EventureBackend.repository.EventRepository;
import com.GrAsp.EventureBackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log
public class EventService {
    public final EventRepository eventRepository;
    public final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Page<Event> getEvents(Pageable pageable, String search, Integer userId, String category) {
        if (userId != null) {
            return eventRepository.findEventsWithSearchAndUserId(search, userId, pageable);
        } else {
            if (category == null || category.isEmpty()) {
                return eventRepository.findEventsWithSearch(search, pageable);
            } else {
                return eventRepository.findEventsWithSearchAndCategory(search, category, pageable);
            }
        }
    }

    public long count(Integer userId) {
        if (userId != null) { // Count events by user ID
            return eventRepository.countEventsWithUserId(userId);
        } else {
            return eventRepository.count(); // Count all events
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
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                throw new RuntimeException("User not found");
            }
            newEvent.setUser(user.get());
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

    public Event updateEvent(CreateEventRequest event, Integer id, Integer userId) {
        try {
            Optional<Event> existingEvent = eventRepository.findById(id);
            if (existingEvent.isEmpty()) {
                throw new RuntimeException("Event not found");
            }

            if (!Objects.equals(existingEvent.get().getUser().getId(), userId)) {
                throw new RuntimeException("You don't have permission to update this event");
            }
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

        } catch (Exception e) {
            throw new RuntimeException("Can't update event, " + e.getMessage());
        }
    }

    public void deleteEvent(Integer id) {
        try {
            Optional<Event> event = eventRepository.findById(id);
            if (event.isEmpty()) {
                throw new RuntimeException("Event not found");
            }
            if (!event.get().getTickets().isEmpty()) {
                throw new RuntimeException("Event has tickets, can't delete");
            }
            Event deletedEvent = event.get();
            deletedEvent.setDeletedAt(OffsetDateTime.now());
            eventRepository.save(deletedEvent);

        } catch (Exception e) {
            throw new RuntimeException("Can't delete event, " + e.getMessage());
        }

    }
}
