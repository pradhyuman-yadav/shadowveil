// src/main/java/com/shadowveil/videoplatform/entity/User.java
package com.shadowveil.videoplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Add
import lombok.AllArgsConstructor; // Add
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "public", uniqueConstraints = {
        @UniqueConstraint(name = "users_email_key", columnNames = {"email"}),
        @UniqueConstraint(name = "users_username_key", columnNames = {"username"})
})
@NoArgsConstructor // Add
@AllArgsConstructor // Add
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50, message = "Username must be less than 50 characters")
    @NotBlank(message = "Username cannot be blank")
    @Column(name = "username", nullable = false, length = 50, unique = true) // Add unique here
    private String username;

    @Size(max = 100, message = "Email must be less than 100 characters")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false, length = 100, unique = true) // Add unique here
    private String email;

    @Size(max = 255)
    @NotBlank(message = "Password cannot be blank") // Important: Store a HASH, not plain text
    @Column(name = "password_hash", nullable = false)
    private String passwordHash; // This should store a *hashed* password

    @NotBlank(message = "Role cannot be blank")
    @Size(max = 20, message="Role can be upto 20 characters")
    @Column(name = "role", nullable = false, length = 20)  // Consider an Enum for roles
    private String role; //  Consider using an Enum for roles

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    // mappedBy refers to the 'instructor' field in the Course entity
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Course> courses;
}