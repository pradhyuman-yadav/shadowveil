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
@Table(name = "sponsorships", schema = "public")
@NoArgsConstructor // Add
@AllArgsConstructor // Add
public class Sponsorship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Correct
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @Size(max = 255)
    @NotNull
    @Column(name = "sponsor_name", nullable = false)
    private String sponsorName;

    @Size(max = 512)
    @Column(name = "sponsor_logo_url", length = 512)
    private String sponsorLogoUrl;

    @Column(name = "details") // No need for length
    private String details;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

}