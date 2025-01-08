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
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enrollments_id_gen")
    @SequenceGenerator(name = "enrollments_id_gen", sequenceName = "enrollments_enrollment_id_seq", allocationSize = 1)
    @Column(name = "enrollment_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ColumnDefault("now()")
    @Column(name = "enrolled_at", nullable = false)
    private OffsetDateTime enrolledAt;

}