package com.GrAsp.EventureBackend.model_staging;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @ColumnDefault("nextval('ticket_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "event_id")
    private Integer eventId;

    @NotNull
    @Column(name = "available", nullable = false)
    private Integer available;

    @NotNull
    @Column(name = "sold", nullable = false)
    private Integer sold;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

}