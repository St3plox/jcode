package com.tveu.jcode.code_service.api.dto;

import com.tveu.jcode.code_service.core.entity.Language;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ProblemSubmissionCreateRequest(

        @NotBlank
        Long userID,

        @NotBlank
        String problemID,

        String code,

        @NotBlank
        Language language
) {
}
