package com.shadowveil.videoplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.net.InetAddress;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "analytics", schema = "public", indexes = {
        @Index(name = "idx_analytics_video_id", columnList = "video_id"),
        @Index(name = "idx_analytics_user_id", columnList = "user_id")
})
public class Analytic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "video_id", nullable = false) // video_id cannot be null
    private Video video;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "user_id")  // user_id can be null
    private User user;

    @Column(name = "view_duration")
    private Integer viewDuration;

    @Column(name = "ip_address")
    private InetAddress ipAddress; // Use InetAddress

    @Column(name = "user_agent", length = Integer.MAX_VALUE)
    private String userAgent;

    @CreationTimestamp
    @Column(name = "watched_at")
    private Instant watchedAt;

}