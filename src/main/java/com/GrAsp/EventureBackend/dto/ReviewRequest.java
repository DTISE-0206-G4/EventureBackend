package com.GrAsp.EventureBackend.dto;

import com.GrAsp.EventureBackend.model.Review;
import lombok.Data;

@Data
public class ReviewRequest {
    private Integer eventId;
    private String description;
    private Integer stars;

    public Review toEntity() { // Implement this method to convert ReviewRequest to Review
        Review review = new Review();
        review.setDescription(description);
        review.setStars(stars);
        return review;
    }
}
