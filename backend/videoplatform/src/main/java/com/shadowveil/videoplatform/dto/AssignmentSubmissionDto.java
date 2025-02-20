// src/main/java/com/shadowveil/videoplatform/dto/AssignmentSubmissionDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public class AssignmentSubmissionDto {

    public record Request(
            @NotNull(message = "Assignment ID cannot be null")
            Integer assignmentId,

            @NotNull(message = "User ID cannot be null")
            Integer userId,

            String submissionText, // Could be @NotBlank, depending on requirements

            BigDecimal grade // Optional, might only be set by instructor
    ) {}

    public record Response(
            Integer id,
            AssignmentDto.Response assignment, // Use existing DTO
            UserDto.Response user,       // Use existing DTO
            String submissionText,
            BigDecimal grade,
            Instant submittedAt
    ) {}
}