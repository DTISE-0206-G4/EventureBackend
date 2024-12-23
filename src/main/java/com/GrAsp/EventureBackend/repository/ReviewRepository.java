package com.GrAsp.EventureBackend.repository;

import com.GrAsp.EventureBackend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByEventId(int id);

    @Query("SELECT COALESCE(AVG(r.stars), 0.0) FROM Review r WHERE r.event.user.id = :userId")
    Double findAverageStarsByUserId(@Param("userId") Integer userId);

    Optional<Review> findReviewByUserIdAndEventId(Integer userId, Integer eventId);
}
