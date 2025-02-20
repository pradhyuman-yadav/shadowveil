// src/main/java/com/shadowveil/videoplatform/entity/Notification.java
package com.shadowveil.videoplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;  // Keep validation here too!
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Add NoArgsConstructor
import lombok.AllArgsConstructor; // Add AllArgsConstructor
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "notifications", schema = "public")
@NoArgsConstructor // Add this
@AllArgsConstructor // Add this for easier object creation in service
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use IDENTITY for auto-increment
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "message", nullable = false) // No need for length = Integer.MAX_VALUE
    private String message;

    @ColumnDefault("false")
    @Column(name = "is_read")
    private Boolean isRead;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;
}