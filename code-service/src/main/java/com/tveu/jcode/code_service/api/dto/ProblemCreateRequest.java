package com.tveu.jcode.code_service.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ProblemCreateRequest(

        @JsonProperty(value = "user_id")
        Long userId,

        @NotBlank
        String title,
        String description
) {
}
