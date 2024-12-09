package com.GrAsp.EventureBackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_gen")
    @SequenceGenerator(name = "transaction_id_gen", sequenceName = "transaction_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

//    @NotNull
//    @Column(name = "user_id", nullable = false)
//    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIncludeProperties({"id","name","email"})
    private User user;

//    @NotNull
//    @Column(name = "ticket_id", nullable = false)
//    private Integer ticketId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ticket_id", nullable = false)  // Foreign key reference to Event
//    @JsonIgnore
//    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
//    @JsonIgnore
    private Ticket ticket;

    @NotNull
    @Column(name = "ticket_price", nullable = false)
    private Double ticketPrice;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "trx_event_discounts", joinColumns = @JoinColumn(name = "transaction_id"), inverseJoinColumns = @JoinColumn(name = "event_discount_id"))
    private Set<EventDiscount> eventDiscounts = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "trx_user_discounts", joinColumns = @JoinColumn(name = "transaction_id"), inverseJoinColumns = @JoinColumn(name = "user_discount_id"))
    private Set<UserDiscount> userDiscounts = new HashSet<>();

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;


    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
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