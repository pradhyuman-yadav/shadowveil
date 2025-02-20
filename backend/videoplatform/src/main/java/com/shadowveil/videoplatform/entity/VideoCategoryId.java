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

@Getter
@Setter
@Embeddable
@NoArgsConstructor // Add this
@AllArgsConstructor // Add this
public class VideoCategoryId implements java.io.Serializable {
    private static final long serialVersionUID = -7131730962884595775L;
    @NotNull
    @Column(name = "video_id", nullable = false)
    private Integer videoId;

    @NotNull
    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VideoCategoryId entity = (VideoCategoryId) o;
        return Objects.equals(this.videoId, entity.videoId) &&
                Objects.equals(this.categoryId, entity.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, categoryId);
    }
}