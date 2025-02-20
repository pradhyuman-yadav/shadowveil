// src/main/java/com/shadowveil/videoplatform/dto/PlaylistDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class PlaylistDto {

    public record Request(
            Integer userId, // Can be null if playlists can exist without an owner

            @NotBlank(message = "Name cannot be blank")
            @Size(max = 100, message = "Name must be less than 100 characters")
            String name,

            String description, // Optional

            @NotBlank(message = "Visibility cannot be blank")
            @Size(max = 20, message = "Visibility must be less than 20 characters")
            String visibility
            // createdAt and updatedAt are handled by the backend
    ) {}

    public record Response(
            Integer id,
            UserDto.Response user, // Nested User DTO
            String name,
            String description,
            String visibility,
            Instant createdAt,
            Instant updatedAt
    ) {}

    //For update DTO
    public record UpdateRequest(
            @NotBlank(message = "Name cannot be blank")
            @Size(max=100, message = "Name must be less than 100 characters")
            String name,

            String description,
            @NotBlank(message = "Visibility cannot be blank")
            String visibility
    ){}
}