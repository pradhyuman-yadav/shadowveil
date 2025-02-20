package com.shadowveil.videoplatform.dto;

import com.shadowveil.videoplatform.Util.ReportStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class ReportDto {

    public record Request(
            @NotNull(message = "Reporter ID cannot be null")
            Integer reporterId,  // Use the ID

            @NotBlank(message = "Reported type cannot be blank")
            @Size(max = 50, message = "Reported type must be less than 50 characters")
            String reportedType,

            @NotNull(message = "Reported ID cannot be null")
            Integer reportedId,

            String reason // Optional
            // status will be set as "PENDING" automatically in service.
    ) {}

    public record Response(
            Integer id,
            UserDto.Response reporter, // Nested DTO
            String reportedType,
            Integer reportedId,
            String reason,
            Instant createdAt,
            String status  // Use String for the enum in the DTO
    ) {}

    public record UpdateStatusRequest(
            @NotNull(message = "Status Can't be null")
            ReportStatus status //Use ENUM
    ) {}
}