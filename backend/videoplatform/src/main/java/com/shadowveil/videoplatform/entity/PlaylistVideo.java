// src/main/java/com/shadowveil/videoplatform/entity/PlaylistVideo.java
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
@Table(name = "playlist_videos", schema = "public")
@NoArgsConstructor // Add
@AllArgsConstructor // Add
public class PlaylistVideo {
    @EmbeddedId
    private PlaylistVideoId id;

    @MapsId("playlistId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @MapsId("videoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @NotNull
    @Column(name = "\"position\"", nullable = false)
    private Integer position;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

}