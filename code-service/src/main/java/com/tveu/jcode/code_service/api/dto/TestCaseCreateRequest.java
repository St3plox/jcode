package com.tveu.jcode.code_service.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TestCaseCreateRequest(

        @NotBlank
        @JsonProperty("problem_id")
        String problemID,

        @NotBlank
        String input,

        @NotBlank
        String output
) {
}
