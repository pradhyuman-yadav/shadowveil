// src/main/java/com/shadowveil/videoplatform/dto/UserProfileDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.Map;

public class UserProfileDto {

    public record Request(
            @NotNull(message = "User ID cannot be null")
            Integer userId, // Use the ID, since it's a 1:1 relationship

            @Size(max = 100, message = "Full name must be less than 100 characters")
            String fullName, // Optional

            String bio,      // Optional

            @Size(max = 512, message = "Profile picture URL must be less than 512 characters")
            String profilePictureUrl, // Optional

            Map<String, Object> socialLinks // Optional
    ) {}

    public record Response(
            Integer userId, // This is also the profile ID
            UserDto.Response user, // Nested User DTO
            String fullName,
            String bio,
            String profilePictureUrl,
            Map<String, Object> socialLinks,
            Instant createdAt,
            Instant updatedAt
    ) {}
    public record UpdateRequest(

            @Size(max = 100, message = "Full name must be less than 100 characters")
            String fullName, // Optional

            String bio,      // Optional

            @Size(max = 512, message = "Profile picture URL must be less than 512 characters")
            String profilePictureUrl, // Optional

            Map<String, Object> socialLinks // Optional
    ) {}
}