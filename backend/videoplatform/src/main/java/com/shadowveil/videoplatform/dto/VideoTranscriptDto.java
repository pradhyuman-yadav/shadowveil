// src/main/java/com/shadowveil/videoplatform/dto/VideoTranscriptDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public class VideoTranscriptDto {

    public record Request(
            @NotNull(message = "Video ID cannot be null")
            Integer videoId,

            @NotBlank(message = "Language cannot be blank")
            @Size(max = 10, message = "Language code must be less than 10 characters")
            String language,

            String transcript // Can be large, but probably shouldn't be *blank*
    ) {}

    public record Response(
            Integer id,
            VideoDto.Response video, // Nested DTO
            String language,
            String transcript,
            Instant createdAt,
            Instant updatedAt
    ) {}

    public record UpdateRequest(
            @NotBlank(message = "Language cannot be blank")
            @Size(max = 10, message = "Language code must be less than 10 characters")
            String language,

            String transcript // Can be large, but probably shouldn't be *blank*
    ) {}
}