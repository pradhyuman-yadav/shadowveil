// src/main/java/com/shadowveil/videoplatform/entity/VideoTagId.java
//No changes needed here, just added comments for clarity.
package com.shadowveil.videoplatform.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import lombok.NoArgsConstructor; // Add
import lombok.AllArgsConstructor; // Add

import java.io.Serializable; // Correct import
import java.util.Objects;

@Getter
@Setter
@Embeddable
@NoArgsConstructor // Add
@AllArgsConstructor // Add
public class VideoTagId implements Serializable {
    private static final long serialVersionUID = -1346685250786734861L;
    @NotNull
    @Column(name = "video_id", nullable = false)
    private Integer videoId;

    @NotNull
    @Column(name = "tag_id", nullable = false)
    private Integer tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VideoTagId entity = (VideoTagId) o;
        return Objects.equals(this.tagId, entity.tagId) &&
                Objects.equals(this.videoId, entity.videoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, videoId);
    }

}