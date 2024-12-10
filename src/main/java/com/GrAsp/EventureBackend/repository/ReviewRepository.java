package com.GrAsp.EventureBackend.repository;

import com.GrAsp.EventureBackend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByEventId(int id);

    @Query("SELECT AVG(r.stars) FROM Review r WHERE r.event.user.id = :userId")
    Double findAverageStarsByUserId(@Param("userId") Integer userId);
}
