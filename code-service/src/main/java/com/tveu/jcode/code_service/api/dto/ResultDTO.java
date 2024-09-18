package com.tveu.jcode.code_service.api.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ResultDTO(

        UUID id,
        UUID submissionID,
        String output,
        String errors
) {
}
