// src/main/java/com/shadowveil/videoplatform/dto/AdvertisementDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.Size;
import java.time.Instant;

public class AdvertisementDto {

    public record Request(
            @Size(max = 255, message = "Title must be less than 255 characters")
            String title,

            String content, // Consider @NotBlank if content should not be empty

            @Size(max = 512, message = "Image URL must be less than 512 characters")
            String imageUrl,

            @Size(max = 512, message = "Target URL must be less than 512 characters")
            String targetUrl,

            Instant startDate,

            Instant endDate
    ) {}

    public record Response(
            Integer id,
            String title,
            String content,
            String imageUrl,
            String targetUrl,
            Instant startDate,
            Instant endDate,
            Instant createdAt
    ) {}
}