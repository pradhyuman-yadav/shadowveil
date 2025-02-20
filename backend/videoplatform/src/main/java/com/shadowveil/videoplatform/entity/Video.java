package com.shadowveil.videoplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "videos", schema = "public", indexes = {
        @Index(name = "idx_videos_user_id", columnList = "user_id")
})
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @Size(max = 255, message = "Video title must be less than 255 characters")
    @NotBlank(message = "Video title cannot be blank")
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 512, message = "Video URL must be less than 512 characters")
    @NotBlank(message = "Video URL cannot be blank")
    @Column(name = "url", nullable = false, length = 512)
    private String url;

    @Size(max = 512, message = "Thumbnail URL must be less than 512 characters")
    @Column(name = "thumbnail_url", length = 512)
    private String thumbnailUrl;

    @NotNull(message = "Video duration cannot be null")
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Size(max = 20)
    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "views")
    private Long views;

    @Column(name = "likes")
    private Long likes;

    @Column(name = "dislikes")
    private Long dislikes;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    // Add this for the Module relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id") // Foreign key column, can be nullable
    private Module module;
}