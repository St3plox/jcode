package com.tveu.jcode.code_service.api.controller;


import com.tveu.jcode.code_service.api.Path;
import com.tveu.jcode.code_service.api.dto.SubmissionCreateRequest;
import com.tveu.jcode.code_service.api.dto.SubmissionDTO;
import com.tveu.jcode.code_service.core.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping(Path.SUBMISSION_POST)
    @ResponseStatus(HttpStatus.CREATED)
    public SubmissionDTO postSubmission(@RequestBody SubmissionCreateRequest createRequest) {

        return submissionService.submit(createRequest);
    }

    @GetMapping(Path.SUBMISSION_GET)
    @ResponseStatus(HttpStatus.OK)
    public SubmissionDTO getSubmission(@PathVariable String id) {

        return submissionService.getByID(id);
    }

    @PatchMapping(Path.SUBMISSION_PATCH)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public SubmissionDTO updateStatus(@RequestParam String id, @RequestParam String status) {

        return submissionService.updateStatusByID(id, status);
    }
}
