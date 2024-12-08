package com.GrAsp.EventureBackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ticket", schema = "public")
@SQLRestriction("deleted_at IS NULL")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_id_gen")
    @SequenceGenerator(name = "ticket_id_gen", sequenceName = "ticket_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

//    @Column(name = "event_id")
//    private Integer eventId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "event_id", nullable = false)  // Foreign key reference to Event
//    @JsonIgnore
//    private Event event;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private Event event;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("ticket")
    @JsonIgnore
    private Set<Transaction> transactions;

    @NotNull
    @Column(name = "available_seat", nullable = false)
    private Integer availableSeat;

    @NotNull
    @Column(name = "sold_seat", nullable = false)
    private Integer soldSeat;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_released", nullable = false)
    private Boolean isReleased;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_closed", nullable = false)
    private Boolean isClosed;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
        soldSeat = 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    @PreRemove
    protected void onRemove() {
        deletedAt = OffsetDateTime.now();
    }

}