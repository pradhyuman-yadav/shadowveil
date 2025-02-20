// src/main/java/com/shadowveil/videoplatform/dto/VideoSubtitleDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class VideoSubtitleDto {

    public record Request(
            @NotNull(message = "Video ID cannot be null")
            Integer videoId,

            @NotBlank(message = "Language cannot be blank")
            @Size(max = 10, message = "Language code must be less than 10 characters")
            String language,

            @Size(max = 512, message = "Subtitle URL must be less than 512 characters")
            String subtitleUrl // Optional
    ) {}

    public record Response(
            Integer id,
            VideoDto.Response video, // Nested DTO
            String language,
            String subtitleUrl,
            Instant createdAt
    ) {}

    public record UpdateRequest(
            @NotBlank(message = "Language cannot be blank")
            @Size(max = 10, message = "Language code must be less than 10 characters")
            String language,

            @Size(max = 512, message = "Subtitle URL must be less than 512 characters")
            String subtitleUrl
    ){}
}