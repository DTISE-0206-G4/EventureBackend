package com.GrAsp.EventureBackend.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_discount", schema = "public")
@SQLRestriction("deleted_at IS NULL")
public class UserDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

//    @Column(name = "transaction_id")
//    private Integer transactionId;

    @Column(name = "title", length = 255, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "is_percentage")
    private Boolean isPercentage;

    @Column(name = "code", nullable = false, length = 255)
    private String code;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expired_at")
    private OffsetDateTime expiredAt;

    @Column(name = "is_used")
    private Boolean isUsed;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
        code = UUID.randomUUID().toString();
        expiredAt = OffsetDateTime.now().plusDays(90);
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
