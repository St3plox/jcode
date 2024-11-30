package com.tveu.jcode.code_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProblemSubmissionCreateRequest(

        @NotNull
        Long userID,

        @NotBlank
        String problemID,

        String code,

        @NotBlank
        String language
) {
}
