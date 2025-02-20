// src/main/java/com/shadowveil/videoplatform/entity/PlaylistVideoId.java
package com.shadowveil.videoplatform.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import lombok.NoArgsConstructor; // Add this
import lombok.AllArgsConstructor; // Add this

import java.util.Objects;
import java.io.Serializable; // Correct import

@Getter
@Setter
@Embeddable
@NoArgsConstructor // Add this
@AllArgsConstructor // Add this
public class PlaylistVideoId implements Serializable { // Correct interface
    private static final long serialVersionUID = 6488075875444444157L;
    @NotNull
    @Column(name = "playlist_id", nullable = false)
    private Integer playlistId;

    @NotNull
    @Column(name = "video_id", nullable = false)
    private Integer videoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PlaylistVideoId entity = (PlaylistVideoId) o;
        return Objects.equals(this.playlistId, entity.playlistId) &&
                Objects.equals(this.videoId, entity.videoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistId, videoId);
    }

}