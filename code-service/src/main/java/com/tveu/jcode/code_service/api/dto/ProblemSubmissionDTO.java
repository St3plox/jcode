package com.tveu.jcode.code_service.api.dto;

import com.tveu.jcode.code_service.core.entity.Language;
import com.tveu.jcode.code_service.core.entity.SubmissionStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ProblemSubmissionDTO(

        UUID id,
        Long userID,
        String problemID,
        String code,
        Language language,
        SubmissionStatus submissionStatus,
        String createdAt,
        String updatedAt
) {
}
