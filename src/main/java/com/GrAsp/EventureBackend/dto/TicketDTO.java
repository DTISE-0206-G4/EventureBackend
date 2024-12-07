package com.GrAsp.EventureBackend.dto;

import com.GrAsp.EventureBackend.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {
    private Integer id;
    private Integer eventId;
    private String name;
    private Double price;
    private Integer availableSeat;
    private Integer soldSeat;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;

    public TicketDTO(Ticket ticket) {
        this.id = ticket.getId();
        this.eventId = ticket.getEvent().getId();
        this.name = ticket.getName();
        this.price = ticket.getPrice();
        this.availableSeat = ticket.getAvailableSeat();
        this.soldSeat = ticket.getSoldSeat();
        this.createdAt = ticket.getCreatedAt();
        this.updatedAt = ticket.getUpdatedAt();
        this.deletedAt = ticket.getDeletedAt();
    }
}
