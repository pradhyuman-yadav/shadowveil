// src/main/java/com/shadowveil/videoplatform/dto/PlaylistVideoDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class PlaylistVideoDto {

    // Request DTO (for creating/updating)
    public record Request(
            @NotNull(message = "Playlist ID cannot be null")
            Integer playlistId,

            @NotNull(message = "Video ID cannot be null")
            Integer videoId,

            @NotNull(message = "Position cannot be null")
            Integer position
    ) {}

    // Response DTO
    public record Response(
            PlaylistDto.Response playlist, // Nested DTOs
            VideoDto.Response video,     // Nested DTOs
            Integer position,
            Instant createdAt
    ) {}

    // DTO for updating position.
    public record UpdatePositionRequest(
            @NotNull(message = "Position cannot be null.")
            Integer position
    ){}
}