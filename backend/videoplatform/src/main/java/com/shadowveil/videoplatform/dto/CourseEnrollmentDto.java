// src/main/java/com/shadowveil/videoplatform/dto/CourseEnrollmentDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class CourseEnrollmentDto {
    public record Request(
            @NotNull(message = "Course ID cannot be null")
            Integer courseId,

            @NotNull(message = "User ID cannot be null")
            Integer userId
    ) {}

    public record Response(
            Integer id,
            CourseDto.Response course, // Use existing DTOs
            UserDto.Response user,
            Instant enrolledAt
    ) {}
}