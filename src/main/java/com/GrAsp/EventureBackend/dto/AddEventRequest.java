package com.GrAsp.EventureBackend.dto;

import lombok.Data;

import java.time.OffsetTime;

@Data
public class AddEventRequest {
    private String title;
    private String description;
    private OffsetTime startDate;
    private OffsetTime endDate;
    private String location;
    private String categories;
}
