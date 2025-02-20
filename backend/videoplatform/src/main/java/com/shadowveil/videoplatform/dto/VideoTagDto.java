// src/main/java/com/shadowveil/videoplatform/dto/VideoTagDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class VideoTagDto {

    // Request DTO (for creating) - Notice, NO VideoTagId here
    public record Request(
            @NotNull(message = "Video ID cannot be null")
            Integer videoId,

            @NotNull(message = "Tag ID cannot be null")
            Integer tagId
    ) {}

    // Response DTO
    public record Response(
            VideoDto.Response video,   // Nested DTO
            TagDto.Response tag,       // Nested DTO
            Instant createdAt
    ) {}

    // NO UpdateRequest DTO is needed.  You can't *update* a VideoTag.
    // You either *create* the association or *delete* it.
}