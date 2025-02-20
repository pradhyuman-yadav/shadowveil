// src/main/java/com/shadowveil/videoplatform/dto/VideoCategoryDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public class VideoCategoryDto {

    // Request DTO (for creating)
    public record Request(
            @NotNull(message = "Video ID cannot be null")
            Integer videoId,

            @NotNull(message = "Category ID cannot be null")
            Integer categoryId
    ) {}

    // Response DTO
    public record Response(
            VideoDto.Response video,    // Nested DTO
            CategoryDto.Response category, // Nested DTO
            Instant createdAt
    ) {}

    // No separate UpdateRequest DTO needed, as there are no updatable fields besides the IDs (which form the PK)
}