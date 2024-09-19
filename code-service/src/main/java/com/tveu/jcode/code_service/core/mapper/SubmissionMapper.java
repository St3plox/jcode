package com.tveu.jcode.code_service.core.mapper;

import com.tveu.jcode.code_service.api.dto.SubmissionCreateRequest;
import com.tveu.jcode.code_service.api.dto.SubmissionDTO;
import com.tveu.jcode.code_service.core.entity.Submission;
import com.tveu.jcode.code_service.core.entity.SubmissionStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SubmissionMapper {

    public Submission map(SubmissionCreateRequest createRequest) {

        return Submission.builder()
                .id(UUID.randomUUID())
                .userID(createRequest.userID())
                .language(createRequest.language())
                .submissionStatus(SubmissionStatus.PENDING)
                .code(createRequest.code())
                .build();
    }

    public SubmissionDTO map(Submission submission) {

        return SubmissionDTO.builder()
                .id(submission.getId())
                .submissionStatus(submission.getSubmissionStatus())
                .code(submission.getCode())
                .userID(submission.getUserID())
                .language(submission.getLanguage())
                .createdAt(submission.getCreatedAt().toString())
                .updatedAt(submission.getUpdatedAt().toString())
                .build();
    }

}
