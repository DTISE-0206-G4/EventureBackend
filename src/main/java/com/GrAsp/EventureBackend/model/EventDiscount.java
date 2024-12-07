package com.GrAsp.EventureBackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "event_discount", schema = "public")
@SQLRestriction("deleted_at IS NULL AND expired_at > CURRENT_TIMESTAMP")
public class EventDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_discount_id_gen")
    @SequenceGenerator(name = "event_discount_id_gen", sequenceName = "event_discount_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "event_id", nullable = false)
//    private Event event;

    @NotNull
    @Column(name = "event_id", nullable = false)
    private Integer eventId;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 255)
    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_percentage", nullable = false)
    private Boolean isPercentage = false;

    @NotNull
    @Column(name = "available", nullable = false)
    private Integer available;

    @NotNull
    @Column(name = "used", nullable = false)
    private Integer used;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "expired_at")
    private OffsetDateTime expiredAt;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_released", nullable = false)
    private Boolean isReleased = false;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_closed", nullable = false)
    private Boolean isClosed = false;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
        used = 0;
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