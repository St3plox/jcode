package com.tveu.jcode.code_service.core.mapper;

import com.tveu.jcode.code_service.api.dto.ResultDTO;
import com.tveu.jcode.code_service.core.entity.Result;
import com.tveu.jcode.code_service.core.exception.ErrorCode;
import com.tveu.jcode.code_service.core.exception.ServiceException;
import com.tveu.jcode.code_service.core.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResultMapper {

    private final SubmissionRepository submissionRepository;

    public ResultDTO map(Result result) {

        return ResultDTO.builder()
                .id(result.getId())
                .output(result.getOutput())
                .errors(result.getErrors())
                .submissionID(result.getSubmission().getId())
                .build();

    }

    public Result map(ResultDTO result) {

        var submission = submissionRepository.findById(result.submissionID())
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, "Submission not found"));

        return Result.builder()
                .id(result.id())
                .output(result.output())
                .errors(result.errors())
                .submission(submission)
                .build();

    }
}
