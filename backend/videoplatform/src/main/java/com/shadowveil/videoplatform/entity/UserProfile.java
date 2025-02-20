package com.shadowveil.videoplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Add
import lombok.AllArgsConstructor; // Add
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "user_profiles", schema = "public")
@NoArgsConstructor // Add
@AllArgsConstructor // Add
public class UserProfile {
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User users;

    @Size(max = 100)
    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "bio") // No need for length
    private String bio;

    @Size(max = 512)
    @Column(name = "profile_picture_url", length = 512)
    private String profilePictureUrl;

    @Column(name = "social_links")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> socialLinks;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp // Use Hibernate annotation
    private Instant updatedAt;

}