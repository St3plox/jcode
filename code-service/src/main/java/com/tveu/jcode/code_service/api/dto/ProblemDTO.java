package com.tveu.jcode.code_service.api.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ProblemDTO(

        UUID id,

        Long userId,

        String title,
        String description,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
