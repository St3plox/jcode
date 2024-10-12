package com.tveu.jcode.code_service.api.dto;

import lombok.Builder;

@Builder
public record TestResultDTO(
        String id,
        String testCaseID,
        Boolean isSuccessful,
        String output,
        String createdAt
) {
}
