package com.tveu.code_exec_worker.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record SubmissionDTO(

        UUID id,
        Long userID,
        String code,
        Language language,
        SubmissionStatus submissionStatus,
        String createdAt,
        String updatedAt

) {
}
