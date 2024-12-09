package com.GrAsp.EventureBackend.service;

import com.GrAsp.EventureBackend.dto.ReviewRequest;
import com.GrAsp.EventureBackend.model.Event;
import com.GrAsp.EventureBackend.model.Review;
import com.GrAsp.EventureBackend.model.User;
import com.GrAsp.EventureBackend.repository.EventRepository;
import com.GrAsp.EventureBackend.repository.ReviewRepository;
import com.GrAsp.EventureBackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log
public class ReviewService {
    public final ReviewRepository reviewRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public List<Review> getAllReviewsByEventId(Integer eventId) {
        return reviewRepository.findByEventId(eventId);
    }

    public Double getAverageStartByOrganizer(Integer userId) {
        return reviewRepository.findAverageStarsByUserId(userId);
    }

    public Review addReview(ReviewRequest req, Integer userId) {
        Review newReview = req.toEntity();
        Optional<Event> event = eventRepository.findById(req.getEventId());
        if (event.isEmpty()) {
            throw new RuntimeException("Event not found");
        }
        log.info("endtime: "+ event.get().getEndTime());
        if (event.get().getEndTime() != null && event.get().getEndTime().isAfter(OffsetDateTime.now())) {
            throw new RuntimeException("Event hasn't ended yet");
        }
        newReview.setEvent(event.get());
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        newReview.setUser(user.get());
        return reviewRepository.save(newReview);
    }
}
