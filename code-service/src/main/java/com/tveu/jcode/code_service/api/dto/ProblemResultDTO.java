package com.tveu.jcode.code_service.api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;


@Builder
public record ProblemResultDTO(

    @JsonProperty("result_dto")
    ResultDTO resultDTO,

    @JsonProperty("test_case_results")
    List<TestResultDTO> testResults

) {
}
