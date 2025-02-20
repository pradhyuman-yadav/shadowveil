// src/main/java/com/shadowveil/videoplatform/dto/NotificationDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class NotificationDto {

    // Inner class for Request DTO
    public record Request(
            @NotNull(message = "User ID cannot be null")
            Integer userId, // ID of the User the notification is for

            @NotBlank(message = "Notification message cannot be blank")
            String message // Content of the notification
            // isRead is not included in the request; it's set server-side
    ) {}

    // Inner class for Response DTO
    public record Response(
            Integer id,
            UserDto.Response user, // Include a nested User DTO (likely a simplified version)
            String message,
            Boolean isRead,
            Instant createdAt
    ) {}
    //Inner class for update Request DTO
    public record UpdateRequest(
            @NotNull(message = "Read Status cannot be null")
            Boolean isRead
    ) {}
}