package com.shadowveil.videoplatform.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class CourseCategoryId implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 4362354908398371107L;
    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CourseCategoryId entity = (CourseCategoryId) o;
        return Objects.equals(this.courseId, entity.courseId) &&
                Objects.equals(this.categoryId, entity.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, categoryId);
    }

}