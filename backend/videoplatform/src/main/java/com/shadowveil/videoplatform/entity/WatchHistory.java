package com.shadowveil.videoplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "watch_history")
public class WatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "watch_history_id_gen")
    @SequenceGenerator(name = "watch_history_id_gen", sequenceName = "watch_history_watch_id_seq", allocationSize = 1)
    @Column(name = "watch_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @ColumnDefault("0")
    @Column(name = "watched_seconds")
    private Integer watchedSeconds;

    @Column(name = "last_watched")
    private OffsetDateTime lastWatched;

}