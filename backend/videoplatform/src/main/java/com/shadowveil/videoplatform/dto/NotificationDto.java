// src/main/java/com/shadowveil/videoplatform/dto/NotificationDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class NotificationDto {

    public record Request(
            @NotNull(message = "User ID cannot be null")
            Integer userId, // User ID, *not* the full User object

            @NotBlank(message = "Message cannot be blank")
            String message
    ) {}

    public record Response(
            Integer id,
            UserDto.Response user, // Use a UserResponseDto.  Good practice to nest DTOs.
            String message,
            Boolean isRead,
            Instant createdAt
    ) {}

    //A DTO for updating the isRead Status only.
    public record UpdateIsReadRequest(
            @NotNull(message = "isRead cannot be null")
            Boolean isRead
    ){}
}