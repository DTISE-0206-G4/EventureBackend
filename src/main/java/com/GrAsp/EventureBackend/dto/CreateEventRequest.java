package com.GrAsp.EventureBackend.dto;

import com.GrAsp.EventureBackend.model.Category;
import com.GrAsp.EventureBackend.model.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateEventRequest {
    private String title;
    private String description;
    private String startTime;
    private String endTime;
    private String location;
    private Set<Integer> categories;
    public Event toEntity() {
        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setLocation(location);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        OffsetDateTime startTime = OffsetDateTime.parse(this.startTime, formatter);
        OffsetDateTime endTime = OffsetDateTime.parse(this.endTime, formatter);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        Set<Category> categories = new HashSet<>();
        event.setCategories(categories);
        return event;
    }
}


