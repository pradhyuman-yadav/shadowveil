// src/main/java/com/shadowveil/videoplatform/dto/CourseDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class CourseDto {

    public record Request(
            @NotNull(message = "Instructor ID cannot be null")
            Integer instructorId,

            @Size(max = 255, message = "Title must be less than 255 characters")
            @NotBlank(message = "Title cannot be blank")
            String title,

            @NotBlank(message = "Description can not be blank")
            String description,

            @NotNull(message = "Category ID cannot be null")
            Integer categoryId
    ) {}

    public record Response(
            Integer id,
            UserDto.Response instructor,
            String title,
            String description,
            Instant createdAt,
            Instant updatedAt,
            CategoryDto.Response category
    ) {}
}