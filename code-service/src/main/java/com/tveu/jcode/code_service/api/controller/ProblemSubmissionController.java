package com.tveu.jcode.code_service.api.controller;

import com.tveu.jcode.code_service.api.Path;
import com.tveu.jcode.code_service.api.dto.ApiResponse;
import com.tveu.jcode.code_service.api.dto.ProblemSubmissionCreateRequest;
import com.tveu.jcode.code_service.api.dto.ProblemSubmissionDTO;
import com.tveu.jcode.code_service.core.service.ProblemSubmissionService;
import com.tveu.jcode.code_service.core.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProblemSubmissionController {

    private final ProblemSubmissionService problemSubmissionService;

    @PostMapping(Path.PROBLEM_SUBMISSION_POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProblemSubmissionDTO> create(@RequestBody @Valid ProblemSubmissionCreateRequest createRequest) {

        var problemSubmission = problemSubmissionService.submit(createRequest);
        return ResponseUtil.success("code submitted successfully", problemSubmission, null);
    }

}
