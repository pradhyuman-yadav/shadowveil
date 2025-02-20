// src/main/java/com/shadowveil/videoplatform/dto/VideoDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class VideoDto {

    public record Request(
            Integer userId, // Use ID for user

            @NotBlank(message = "Video title cannot be blank")
            @Size(max = 255, message = "Video title must be less than 255 characters")
            String title,

            String description,

            @NotBlank(message = "Video URL cannot be blank")
            @Size(max = 512, message = "Video URL must be less than 512 characters")
            String url,

            @Size(max = 512, message = "Thumbnail URL must be less than 512 characters")
            String thumbnailUrl,

            @NotNull(message = "Video duration cannot be null")
            Integer duration,

            @Size(max = 20)
            String status,

            Integer moduleId
    ) {}

    public record Response(
            Integer id,
            UserDto.Response user,  // Use nested DTO
            String title,
            String description,
            String url,
            String thumbnailUrl, // Correct field name
            Integer duration,
            String status,
            Long views,
            Long likes,
            Long dislikes,
            Instant createdAt,
            Instant updatedAt,
            ModuleDto.Response module // Use nested DTO

    ) {}
}