// src/main/java/com/shadowveil/videoplatform/dto/SubscriptionDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public class SubscriptionDto {

    public record Request(
            Integer userId, // Can be null if a subscription can exist without a user initially

            @NotBlank(message = "Plan cannot be blank")
            @Size(max = 50, message = "Plan must be less than 50 characters")
            String plan,

            @NotNull(message = "Start date cannot be null")
            Instant startDate
            // End date is optional, handled in service.
            // Status is handled in backend.

    ) {}

    public record Response(
            Integer id,
            UserDto.Response user,  // Nested User DTO.  Good practice!
            String plan,
            Instant startDate,
            Instant endDate, // Correct field name in DTO
            String status
    ) {}

    //DTO for update.
    public record UpdateRequest(
            @NotBlank(message = "Plan cannot be blank")
            String plan,
            Instant startDate,
            Instant endDate, // Now optional.
            @NotBlank(message = "Status cannot be blank")
            String status
    ){}
}