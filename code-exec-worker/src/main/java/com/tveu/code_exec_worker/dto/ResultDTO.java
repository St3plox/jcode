package com.tveu.code_exec_worker.dto;

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
