package com.shadowveil.videoplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "videos", schema = "public", indexes = {
        @Index(name = "idx_videos_user_id", columnList = "user_id")
})
public class Video {
    @Id
    @ColumnDefault("nextval('videos_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 512)
    @NotNull
    @Column(name = "url", nullable = false, length = 512)
    private String url;

    @Size(max = 512)
    @Column(name = "thumbnail_url", length = 512)
    private String thumbnailUrl;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Size(max = 20)
    @ColumnDefault("'public'")
    @Column(name = "status", length = 20)
    private String status;

    @ColumnDefault("0")
    @Column(name = "views")
    private Long views;

    @ColumnDefault("0")
    @Column(name = "likes")
    private Long likes;

    @ColumnDefault("0")
    @Column(name = "dislikes")
    private Long dislikes;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

}