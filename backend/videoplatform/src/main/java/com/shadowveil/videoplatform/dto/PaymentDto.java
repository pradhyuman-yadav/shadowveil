// src/main/java/com/shadowveil/videoplatform/dto/PaymentDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

public class PaymentDto {

    public record Request(
            Integer userId, // Can be null if payment is not directly tied to a user (e.g., guest checkout)

            Integer subscriptionId, // Can be null if not a subscription payment

            @NotNull(message = "Amount cannot be null")
            @Positive(message = "Amount must be positive")
            BigDecimal amount,

            @NotBlank(message = "Currency cannot be blank")
            @Size(min = 3, max = 3, message = "Currency must be a 3-letter code")
            String currency,

            @NotBlank(message = "Payment method cannot be blank")
            @Size(max = 50, message = "Payment method must be less than 50 characters")
            String paymentMethod
            // status is not here, as it will be set on backend.
    ) {}

    public record Response(
            Integer id,
            UserDto.Response user, // Nested User DTO
            SubscriptionDto.Response subscription,  //Nested Subscription DTO
            BigDecimal amount,
            String currency,
            String status,
            String paymentMethod,
            Instant createdAt
    ) {}

    // DTO For updating Status only.
    public record UpdateStatusRequest(
            @NotBlank(message = "Status Can't be Blank")
            String status
    ) {}
}