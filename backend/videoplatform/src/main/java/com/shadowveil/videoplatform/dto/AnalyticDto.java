// src/main/java/com/shadowveil/videoplatform/dto/AnalyticDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotNull;

import java.net.InetAddress;
import java.time.Instant;

public class AnalyticDto {

    public record Request(
            @NotNull(message = "Video ID cannot be null")
            Integer videoId,

            Integer userId, // Can be null

            Integer viewDuration, // Consider making this required if it's essential

            InetAddress ipAddress, // Validate this appropriately in a custom validator if needed

            String userAgent  // Consider @NotBlank if userAgent is always required
    ) {}

    public record Response(
            Integer id,
            VideoDto.Response video, // Use existing DTO
            UserDto.Response user, // Use existing DTO
            Integer viewDuration,
            InetAddress ipAddress,
            String userAgent,
            Instant watchedAt
    ) {}
}