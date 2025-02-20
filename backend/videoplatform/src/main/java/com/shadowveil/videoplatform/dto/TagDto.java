// src/main/java/com/shadowveil/videoplatform/dto/TagDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class TagDto {

    public record Request(
            @NotBlank(message = "Tag name cannot be blank")
            @Size(max = 100, message = "Tag name must be less than 100 characters")
            String name
    ) {}

    public record Response(
            Integer id,
            String name,
            Instant createdAt,
            Instant updatedAt
    ) {}
    //for Update Request
    public record UpdateRequest(
            @NotBlank(message = "Tag name cannot be blank")
            @Size(max = 100, message = "Tag Name must be less than 100 character")
            String name
    ){}
}