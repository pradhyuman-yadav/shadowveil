package com.shadowveil.videoplatform.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class VideoTagId implements java.io.Serializable {
    private static final long serialVersionUID = -1346685250786734861L;
    @NotNull
    @Column(name = "video_id", nullable = false)
    private Integer videoId;

    @NotNull
    @Column(name = "tag_id", nullable = false)
    private Integer tagId;

    // No-argument constructor (REQUIRED by JPA)
    public VideoTagId() {}

    // Constructor with arguments (HIGHLY RECOMMENDED)
    public VideoTagId(Integer videoId, Integer tagId) {
        this.videoId = videoId;
        this.tagId = tagId;
    }

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