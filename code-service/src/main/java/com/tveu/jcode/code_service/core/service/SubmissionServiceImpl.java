package com.tveu.jcode.code_service.core.service;


import com.tveu.jcode.code_service.api.dto.SubmissionCreateRequest;
import com.tveu.jcode.code_service.api.dto.SubmissionDTO;
import com.tveu.jcode.code_service.core.entity.SubmissionStatus;
import com.tveu.jcode.code_service.core.exception.ErrorCode;
import com.tveu.jcode.code_service.core.exception.ServiceException;
import com.tveu.jcode.code_service.core.kafka.KafkaProducer;
import com.tveu.jcode.code_service.core.kafka.KafkaTopic;
import com.tveu.jcode.code_service.core.mapper.SubmissionMapper;
import com.tveu.jcode.code_service.core.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final SubmissionMapper submissionMapper;

    private final KafkaProducer kafkaProducer;

    @Override
    public SubmissionDTO submit(SubmissionCreateRequest createRequest) {

        var submission = submissionRepository.save(submissionMapper.map(createRequest));

        var submissionDTO = submissionMapper.map(submission);
        kafkaProducer.sendMessage(submissionDTO, KafkaTopic.SUBMISSION);

        return submissionDTO;
    }

    @Override
    public SubmissionDTO getByID(String id) {

        var submission = submissionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, id + " submission not found"));

        return submissionMapper.map(submission);
    }

    @Override
    public SubmissionDTO updateStatusByID(String id, String status) {

        var submission = submissionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, id + " submission not found"));


        submission.setSubmissionStatus(SubmissionStatus.valueOf(status));
        var newSubmission = submissionRepository.save(submission);

        return submissionMapper.map(newSubmission);
    }
}
