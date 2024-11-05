package com.tveu.jcode.code_service.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record TestResultDTO(
        String id,

        @JsonProperty("test_case_id")
        String testCaseID,

        @JsonProperty("is_successful")
        Boolean isSuccessful,

        String output,

        @JsonProperty("created_at")
        String createdAt
) {
}
