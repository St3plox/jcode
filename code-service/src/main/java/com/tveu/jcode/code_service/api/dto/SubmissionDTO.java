package com.tveu.jcode.code_service.api.dto;

import com.tveu.jcode.code_service.core.entity.Language;
import com.tveu.jcode.code_service.core.entity.SubmissionStatus;
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
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}
