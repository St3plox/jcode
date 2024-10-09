package com.tveu.jcode.code_service.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record ProblemSubmissionKafkaDTO(

        @JsonProperty("submission_dto")
        ProblemSubmissionDTO submissionDTO,

        @JsonProperty("test_cases")
        List<TestCaseDTO> testCases

) {
}
