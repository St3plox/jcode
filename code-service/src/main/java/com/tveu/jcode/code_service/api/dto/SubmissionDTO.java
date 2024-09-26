package com.tveu.jcode.code_service.api.dto;

import java.util.UUID;

import com.tveu.jcode.code_service.core.entity.Language;
import com.tveu.jcode.code_service.core.entity.SubmissionStatus;

import lombok.Builder;

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
