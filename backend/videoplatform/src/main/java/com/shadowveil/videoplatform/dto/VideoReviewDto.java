// src/main/java/com/shadowveil/videoplatform/dto/VideoReviewDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class VideoReviewDto {

    public record Request(
            @NotNull(message = "Video ID cannot be null")
            Integer videoId,

            @NotNull(message = "User ID cannot be null")
            Integer userId,

            @NotNull(message = "Rating cannot be null")
            @Min(value = 1, message = "Rating must be at least 1")
            @Max(value = 5, message = "Rating must be at most 5")
            Short rating,

            String reviewText // Optional
    ) {}

    public record Response(
            Integer id,
            VideoDto.Response video, // Nested DTOs
            UserDto.Response user,  // Nested DTOs
            Short rating,
            String reviewText,
            Instant createdAt,
            Instant updatedAt
    ) {}
    public record UpdateRequest(
            @NotNull(message = "Rating cannot be null")
            @Min(value = 1, message = "Rating must be at least 1")
            @Max(value = 5, message = "Rating must be at most 5")
            Short rating,
            String reviewText
    ) {}
}