package com.tveu.jcode.code_service.core.mapper;

import com.tveu.jcode.code_service.api.dto.ProblemSubmissionCreateRequest;
import com.tveu.jcode.code_service.api.dto.ProblemSubmissionDTO;
import com.tveu.jcode.code_service.core.entity.Language;
import com.tveu.jcode.code_service.core.entity.ProblemSubmission;
import com.tveu.jcode.code_service.core.entity.SubmissionStatus;
import com.tveu.jcode.code_service.core.exception.ErrorCode;
import com.tveu.jcode.code_service.core.exception.ServiceException;
import com.tveu.jcode.code_service.core.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProblemSubmissionMapper {

    private final ProblemRepository problemRepository;

    public ProblemSubmission map(ProblemSubmissionCreateRequest createRequest) {

        var problem = problemRepository.findById(UUID.fromString(createRequest.problemID()))
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, createRequest.problemID()));

        return ProblemSubmission.builder()
                .id(UUID.randomUUID())
                .userID(createRequest.userID())
                .problem(problem)
                .language(Language.valueOf(createRequest.language()))
                .submissionStatus(SubmissionStatus.PENDING)
                .code(createRequest.code())
                .build();
    }

    public ProblemSubmissionDTO map(ProblemSubmission submission) {

        return ProblemSubmissionDTO.builder()
                .id(submission.getId())
                .submissionStatus(submission.getSubmissionStatus())
                .code(submission.getCode())
                .problemID(submission.getProblem().getId().toString())
                .userID(submission.getUserID())
                .language(submission.getLanguage())
                .createdAt(submission.getCreatedAt().toString())
                .updatedAt(submission.getUpdatedAt().toString())
                .build();
    }
}
