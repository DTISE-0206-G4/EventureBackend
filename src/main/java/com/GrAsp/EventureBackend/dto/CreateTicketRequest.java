package com.GrAsp.EventureBackend.dto;

import com.GrAsp.EventureBackend.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateTicketRequest {
    private Integer eventId;
    private String name;
    private Double price;
    private Integer availableSeat;
    private Boolean isReleased;
    private Boolean isClosed;

    public Ticket toEntity() {
        Ticket ticket = new Ticket();
//        ticket.setEventId(eventId);
        ticket.setName(name);
        ticket.setPrice(price);
        ticket.setAvailableSeat(availableSeat);
        ticket.setIsReleased(isReleased);
        ticket.setIsClosed(isClosed);
        return ticket;
    }
}
