// src/main/java/com/shadowveil/videoplatform/dto/MessageDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class MessageDto {

    public record Request(
            @NotNull(message = "Sender ID cannot be null")
            Integer senderId,

            @NotNull(message = "Receiver ID cannot be null")
            Integer receiverId,

            @NotBlank(message = "Message content cannot be blank")
            String content
    ) {}

    public record Response(
            Integer id,
            UserDto.Response sender, // Use existing DTO
            UserDto.Response receiver, // Use existing DTO
            String content,
            Instant sentAt,
            Boolean isRead
    ) {}
}