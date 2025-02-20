package com.shadowveil.videoplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "advertisements", schema = "public")
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255, message = "Title must be less than 255 characters")
    @Column(name = "title")
    private String title;

    @Column(name = "content", length = Integer.MAX_VALUE)
    private String content;

    @Size(max = 512, message = "Image URL must be less than 512 characters")
    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Size(max = 512, message = "Target URL must be less than 512 characters")
    @Column(name = "target_url", length = 512)
    private String targetUrl;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
}