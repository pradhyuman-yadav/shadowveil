// src/main/java/com/shadowveil/videoplatform/dto/UserDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class UserDto {

    public record Request(
            @NotBlank(message = "Username cannot be blank")
            @Size(max = 50, message = "Username must be less than 50 characters")
            String username,

            @NotBlank(message = "Email cannot be blank")
            @Email(message = "Invalid email format")
            @Size(max = 100, message = "Email must be less than 100 characters")
            String email,

            @NotBlank(message = "Password cannot be blank")
            @Size(min = 8, message = "Password must be at least 8 characters")
            String password, // Plain text password in the request

            @NotBlank(message = "Role cannot be blank")
            String role
    ) {}

    public record Response(
            Integer id,
            String username,
            String email,
            // DO NOT INCLUDE passwordHash in the response!
            String role,
            Instant createdAt,
            Instant updatedAt
    ) {}
}