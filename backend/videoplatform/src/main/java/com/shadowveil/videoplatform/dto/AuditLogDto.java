// src/main/java/com/shadowveil/videoplatform/dto/AuditLogDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class AuditLogDto {

    public record Request(
            @NotNull(message = "Admin ID cannot be null")
            Integer adminId,

            @NotBlank(message = "Action cannot be blank")
            String action,

            String details // Optional, could be blank
    ) {}

    public record Response(
            Integer id,
            UserDto.Response admin, // Use existing DTO
            String action,
            String details,
            Instant createdAt
    ) {}
}