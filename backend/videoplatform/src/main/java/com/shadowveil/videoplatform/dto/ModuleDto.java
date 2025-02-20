// src/main/java/com/shadowveil/videoplatform/dto/ModuleDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class ModuleDto {

    public record Request(
            @NotNull(message = "Course ID cannot be null")
            Integer courseId,  // Use the ID for the course

            @NotBlank(message = "Module title cannot be blank")
            @Size(max = 255, message = "Module title must be less than 255 characters")
            String title,

            String description, // Optional

            Integer position // Optional (can be null)
    ) {}

    public record Response(
            Integer id,
            CourseDto.Response course, // Use the existing CourseResponseDto
            String title,
            String description,
            Integer position,
            Instant createdAt,
            Instant updatedAt
    ) {}
}