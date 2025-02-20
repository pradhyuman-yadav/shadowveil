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

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "video_subtitles", schema = "public")
@NoArgsConstructor // Add
@AllArgsConstructor // Add
public class VideoSubtitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Correct
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @Size(max = 10)
    @NotNull
    @Column(name = "language", nullable = false, length = 10)
    private String language;

    @Size(max = 512)
    @Column(name = "subtitle_url", length = 512)
    private String subtitleUrl;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

}