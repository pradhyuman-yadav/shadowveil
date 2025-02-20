package com.shadowveil.videoplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Add
import lombok.AllArgsConstructor; // Add
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "payments", schema = "public", indexes = {
        @Index(name = "idx_payments_user_id", columnList = "user_id")
})
@NoArgsConstructor // Add
@AllArgsConstructor // Add
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Correct way
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE) // Or SET NULL, depending on requirements
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Size(max = 3)
    @ColumnDefault("'USD'")
    @Column(name = "currency", length = 3)
    private String currency;

    @Size(max = 20)
    @ColumnDefault("'completed'") // Consider 'pending' as initial status
    @Column(name = "status", length = 20)
    private String status;

    @Size(max = 50)
    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

}