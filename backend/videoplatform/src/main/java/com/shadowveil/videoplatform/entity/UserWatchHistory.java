package com.shadowveil.videoplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Add
import lombok.AllArgsConstructor; // Add
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user_watch_history", schema = "public")
@NoArgsConstructor // Add
@AllArgsConstructor // Add
public class UserWatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Correct
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "watched_at")
    private Instant watchedAt;

    @Column(name = "duration_watched")
    private Integer durationWatched;

}