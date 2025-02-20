// src/main/java/com/shadowveil/videoplatform/dto/CategoryDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class CategoryDto {

    public record Request(
            @NotBlank(message = "Category name cannot be blank")
            @Size(max = 100, message = "Category name must be less than 100 characters")
            String name,

            String description // Description can be optional
    ) {}

    public record Response(
            Integer id,
            String name,
            String description,
            Instant createdAt
    ) {}
}