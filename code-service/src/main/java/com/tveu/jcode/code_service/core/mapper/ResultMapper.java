package com.tveu.jcode.code_service.core.mapper;

import com.tveu.jcode.code_service.api.dto.ResultDTO;
import com.tveu.jcode.code_service.core.entity.Result;
import com.tveu.jcode.code_service.core.exception.ErrorCode;
import com.tveu.jcode.code_service.core.exception.ServiceException;
import com.tveu.jcode.code_service.core.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ResultMapper {

    private final SubmissionRepository submissionRepository;

    public ResultDTO map(Result result) {

        return ResultDTO.builder()
                .id(result.getId().toString())
                .output(result.getOutput())
                .errors(result.getErrors())
                .submissionID(result.getSubmission().getId().toString())
                .build();

    }

    public Result map(ResultDTO result) {

        var submission = submissionRepository.findById(UUID.fromString(result.submissionID()))
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, "Submission not found "));


        return Result.builder()
                .id(UUID.fromString(result.id()))
                .output(result.output())
                .errors(result.errors())
                .submission(submission)
                .build();

    }
}
