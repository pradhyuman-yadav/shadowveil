// src/main/java/com/shadowveil/videoplatform/dto/CertificateDto.java
package com.shadowveil.videoplatform.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class CertificateDto {

    public record Request(
            @NotNull(message = "Enrollment ID cannot be null")
            Integer enrollmentId,

            @Size(max = 512, message = "Certificate URL must be less than 512 characters")
            String certificateUrl // Optional for creation, could be generated later
    ) {}

    public record Response(
            Integer id,
            CourseEnrollmentDto.Response enrollment, // Use existing DTO
            String certificateUrl,
            Instant issuedAt
    ) {}
}