package com.tveu.jcode.code_service.api.controller;


import com.tveu.jcode.code_service.api.Path;
import com.tveu.jcode.code_service.api.dto.ApiResponse;
import com.tveu.jcode.code_service.api.dto.SubmissionCreateRequest;
import com.tveu.jcode.code_service.api.dto.SubmissionDTO;
import com.tveu.jcode.code_service.core.service.SubmissionService;
import com.tveu.jcode.code_service.core.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping(Path.SUBMISSION_POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SubmissionDTO> post(@RequestBody @Valid SubmissionCreateRequest createRequest) {

        var submission = submissionService.submit(createRequest);
        return ResponseUtil.success("code submitted successfully", submission, null);

    }

    @GetMapping(Path.SUBMISSION_GET)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<SubmissionDTO> get(@PathVariable String id) {

        var submission = submissionService.getByID(id);
        return ResponseUtil.success("submission retrieved successfully", submission, null);
    }

    @PatchMapping(Path.SUBMISSION_PATCH)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<SubmissionDTO> update(@RequestParam String id, @RequestParam String status) {

        var submission = submissionService.updateStatusByID(id, status);
        return ResponseUtil.success("submission status successfully", submission, null);
    }
}
