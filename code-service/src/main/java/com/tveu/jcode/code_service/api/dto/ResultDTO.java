package com.tveu.jcode.code_service.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


@Builder
public record ResultDTO(

        String id,

        @JsonProperty("submission_id")
        String submissionID,
        String output,
        String errors
) {
}
