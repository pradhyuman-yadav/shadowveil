package com.shadowveil.videoplatform.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
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
    @ColumnDefault("nextval('analytics_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "video_id")
    private Video video;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "view_duration")
    private Integer viewDuration;

    @Column(name = "ip_address")
    private InetAddress ipAddress;

    @Column(name = "user_agent", length = Integer.MAX_VALUE)
    private String userAgent;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "watched_at")
    private Instant watchedAt;

}