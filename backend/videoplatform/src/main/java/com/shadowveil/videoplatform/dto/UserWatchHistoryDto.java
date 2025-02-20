// src/main/java/com/shadowveil/videoplatform/dto/UserWatchHistoryDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class UserWatchHistoryDto {

    public record Request(
            @NotNull(message = "User ID cannot be null")
            Integer userId,

            @NotNull(message = "Video ID cannot be null")
            Integer videoId,

            Integer durationWatched // Optional
    ) {}

    public record Response(
            Integer id,
            UserDto.Response user, // Nested DTOs
            VideoDto.Response video, // Nested DTOs
            Instant watchedAt,
            Integer durationWatched
    ) {}
    //For Update Request
    public record UpdateRequest(
            @NotNull(message = "Duration watched can't be null")
            Integer durationWatched
    ) {}
}