package com.shadowveil.videoplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Add
import lombok.AllArgsConstructor; // Add
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tags", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "tags_name_key", columnNames = {"name"})
})
@NoArgsConstructor // Add
@AllArgsConstructor // Add
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Correct
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100, unique = true) // Add unique constraint here
    private String name;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp // Use Hibernate's annotation
    private Instant updatedAt;

}