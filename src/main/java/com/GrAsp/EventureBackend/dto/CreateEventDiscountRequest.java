package com.GrAsp.EventureBackend.dto;

import com.GrAsp.EventureBackend.model.EventDiscount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventDiscountRequest {
    private Integer eventId;
    private String title;
    private String description;
    private String code;
    private Double amount;
    private Boolean isPercentage;
    private Integer available;
    private Boolean isReleased;
    private Boolean isClosed;
    private String expiredAt;

    public EventDiscount toEntity() {
        EventDiscount eventDiscount = new EventDiscount();
        eventDiscount.setEventId(eventId);
        eventDiscount.setTitle(title);
        eventDiscount.setDescription(description);
        eventDiscount.setCode(code);
        eventDiscount.setAmount(amount);
        eventDiscount.setIsPercentage(isPercentage);
        eventDiscount.setAvailable(available);
        eventDiscount.setIsReleased(isReleased);
        eventDiscount.setIsClosed(isClosed);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        OffsetDateTime expiredAt = OffsetDateTime.parse(this.expiredAt, formatter);
        eventDiscount.setExpiredAt(expiredAt);
        return eventDiscount;
    }

}
