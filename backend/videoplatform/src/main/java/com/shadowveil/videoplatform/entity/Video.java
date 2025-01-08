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
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "videos_id_gen")
    @SequenceGenerator(name = "videos_id_gen", sequenceName = "videos_video_id_seq", allocationSize = 1)
    @Column(name = "video_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "course_id", nullable = false)
    private Course courseId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "video_url", nullable = false, length = Integer.MAX_VALUE)
    private String videoUrl;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "\"position\"")
    private Integer position;

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @ColumnDefault("now()")
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

}