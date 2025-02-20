// src/main/java/com/shadowveil/videoplatform/dto/CommentDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

public class CommentDto {

    public record Request(
            Integer videoId, // Can be null if it's a reply

            Integer userId, // Can be null if anonymous comments are allowed (but you'd need auth)

            @NotBlank(message = "Comment content cannot be blank")
            String content,

            Integer parentCommentId // Can be null for top-level comments
    ) {}

    public record Response(
            Integer id,
            VideoDto.Response video, // Use existing DTO
            UserDto.Response user, // Use existing DTO
            String content,
            Integer parentCommentId, // ID of the parent comment
            Instant createdAt,
            Instant updatedAt,
            List<Response> replies // Recursive DTO for replies!
    ) {}
}