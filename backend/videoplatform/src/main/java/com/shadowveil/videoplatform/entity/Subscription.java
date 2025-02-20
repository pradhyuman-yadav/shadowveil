// src/main/java/com/shadowveil/videoplatform/entity/Subscription.java
package com.shadowveil.videoplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "subscriptions", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "user_id")
    private User user;

    @Size(max = 50)
    @NotNull
    @Column(name = "plan", nullable = false, length = 50)
    private String plan;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate; // Correct field name

    @Size(max = 20)
    @ColumnDefault("'active'")
    @Column(name = "status", length = 20)
    private String status;
}