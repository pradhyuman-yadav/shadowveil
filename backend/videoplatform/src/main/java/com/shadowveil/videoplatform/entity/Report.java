package com.shadowveil.videoplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Add
import lombok.AllArgsConstructor; // Add
import org.hibernate.annotations.ColumnDefault;
import com.shadowveil.videoplatform.Util.ReportStatus; // Import the enum
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "reports", schema = "public")
@NoArgsConstructor // Add
@AllArgsConstructor // Add
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Correct
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "reporter_id", nullable = false)
    private User reporter;

    @Size(max = 50)
    @NotNull
    @Column(name = "reported_type", nullable = false, length = 50)
    private String reportedType;

    @NotNull
    @Column(name = "reported_id", nullable = false)
    private Integer reportedId;

    @Column(name = "reason") // No need for length = Integer.MAX_VALUE
    private String reason;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @Size(max = 20)
    @ColumnDefault("'PENDING'") // Keep as String in the database
    @Column(name = "status", length = 20)
    private String status; // Keep as String in the database

}