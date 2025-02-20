// src/main/java/com/shadowveil/videoplatform/dto/SponsorshipDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class SponsorshipDto {

    public record Request(
            @NotNull(message = "Video ID cannot be null")
            Integer videoId, // Use the ID

            @NotBlank(message = "Sponsor name cannot be blank")
            @Size(max = 255, message = "Sponsor name must be less than 255 characters")
            String sponsorName,

            @Size(max = 512, message = "Sponsor logo URL must be less than 512 characters")
            String sponsorLogoUrl, // Optional

            String details // Optional
    ) {}

    public record Response(
            Integer id,
            VideoDto.Response video, // Nested DTO
            String sponsorName,
            String sponsorLogoUrl,
            String details,
            Instant createdAt
    ) {}

    // For Update Request.
    public record UpdateRequest(
            @NotBlank(message = "Sponsor name cannot be blank")
            @Size(max = 255, message = "Sponsor name must be less than 255 characters")
            String sponsorName,

            @Size(max = 512, message = "Sponsor logo URL must be less than 512 characters")
            String sponsorLogoUrl, // Optional

            String details // Optional
    ){}
}