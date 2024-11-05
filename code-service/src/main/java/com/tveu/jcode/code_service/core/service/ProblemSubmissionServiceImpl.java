package com.tveu.jcode.code_service.core.service;

import com.tveu.jcode.code_service.api.dto.ProblemSubmissionCreateRequest;
import com.tveu.jcode.code_service.api.dto.ProblemSubmissionDTO;
import com.tveu.jcode.code_service.api.dto.ProblemSubmissionKafkaDTO;
import com.tveu.jcode.code_service.core.entity.ProblemSubmission;
import com.tveu.jcode.code_service.core.entity.SubmissionStatus;
import com.tveu.jcode.code_service.core.exception.ErrorCode;
import com.tveu.jcode.code_service.core.exception.ServiceException;
import com.tveu.jcode.code_service.core.kafka.KafkaProducer;
import com.tveu.jcode.code_service.core.kafka.KafkaTopic;
import com.tveu.jcode.code_service.core.mapper.ProblemSubmissionMapper;
import com.tveu.jcode.code_service.core.mapper.TestCaseMapper;
import com.tveu.jcode.code_service.core.repository.ProblemSubmissionRepository;
import com.tveu.jcode.code_service.core.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProblemSubmissionServiceImpl implements ProblemSubmissionService {

    private final ProblemSubmissionRepository problemSubmissionRepository;
    private final TestCaseRepository testCaseRepository;
    private final TestCaseMapper testCaseMapper;

    private final ProblemSubmissionMapper submissionMapper;

    private final KafkaProducer kafkaProducer;

    @Override
    public ProblemSubmissionDTO submit(ProblemSubmissionCreateRequest createRequest) {

        ProblemSubmission problemSubmission = submissionMapper.map(createRequest);

        var savedProblemSubmission = problemSubmissionRepository.save(problemSubmission);
        var problemSubmissionDTO = submissionMapper.map(savedProblemSubmission);

        var testCases = testCaseRepository.findAllByProblem(problemSubmission.getProblem());

        var dto = ProblemSubmissionKafkaDTO
                .builder()
                .submissionDTO(problemSubmissionDTO)
                .testCases(testCaseMapper.map(testCases))
                .build();

        kafkaProducer.sendMessage(dto, KafkaTopic.PROBLEM_SUBMISSION);

        return problemSubmissionDTO;
    }

    @Override
    public ProblemSubmissionDTO getByID(String id) {
        var problemSubmission = problemSubmissionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, id + " problem submission not found"));
        return submissionMapper.map(problemSubmission);
    }

    @Override
    public ProblemSubmissionDTO updateStatusByID(String id, String status) {
        var problemSubmission = problemSubmissionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, id + " problem submission not found"));

        problemSubmission.setSubmissionStatus(SubmissionStatus.valueOf(status));
        var updatedSubmission = problemSubmissionRepository.save(problemSubmission);

        return submissionMapper.map(updatedSubmission);
    }
}
