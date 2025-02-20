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
import org.hibernate.annotations.UpdateTimestamp; // Add this for updatedAt

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "playlists", schema = "public", indexes = {
        @Index(name = "idx_playlists_user_id", columnList = "user_id")
})
@NoArgsConstructor // Add
@AllArgsConstructor // Add
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Correct way
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL) // Changed to SET_NULL
    @JoinColumn(name = "user_id")
    private User user;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description") // No need for length = Integer.MAX_VALUE
    private String description;

    @Size(max = 20)
    @ColumnDefault("'public'")
    @Column(name = "visibility", length = 20)
    private String visibility;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;


    @Column(name = "updated_at")
    @UpdateTimestamp // Use Hibernate's annotation for auto-update
    private Instant updatedAt;
}