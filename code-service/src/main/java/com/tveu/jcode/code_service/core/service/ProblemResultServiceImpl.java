package com.tveu.jcode.code_service.core.service;

import com.tveu.jcode.code_service.api.dto.ProblemResultDTO;
import com.tveu.jcode.code_service.core.entity.ProblemResult;
import com.tveu.jcode.code_service.core.entity.SubmissionStatus;
import com.tveu.jcode.code_service.core.exception.ErrorCode;
import com.tveu.jcode.code_service.core.exception.ServiceException;
import com.tveu.jcode.code_service.core.mapper.TestResultMapper;
import com.tveu.jcode.code_service.core.repository.ProblemResultRepository;
import com.tveu.jcode.code_service.core.repository.ProblemSubmissionRepository;
import com.tveu.jcode.code_service.core.repository.TestResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProblemResultServiceImpl implements ProblemResultService {

    private final ProblemResultRepository problemResultRepository;
    private final TestResultRepository testResultRepository;
    private final TestResultMapper testResultMapper;
    private final ProblemSubmissionRepository submissionRepository;

    @Override
    public ProblemResult create(ProblemResultDTO resultEvent) {

        ProblemResult problemResult = new ProblemResult(
                UUID.fromString(resultEvent.resultDTO().id()),
                null,
                resultEvent.resultDTO().output(),
                resultEvent.resultDTO().errors(),
                null);

        log.info("saving test results");
        testResultRepository.saveAll(testResultMapper.map(resultEvent.testResults()));
        System.out.println(UUID.fromString(resultEvent.resultDTO().submissionID()));
        System.out.println("--------------------------------------------");

        var problemSubmission = submissionRepository.findById(UUID.fromString(resultEvent.resultDTO().submissionID()))
                .orElseThrow(() -> new ServiceException(ErrorCode.OBJECT_NOT_FOUND, "Submission not found"));



        log.info("saving resultDTO");

        problemResult.setSubmission(problemSubmission);
        problemResult = problemResultRepository.save(problemResult);

        if (resultEvent.resultDTO().errors().isEmpty()) {

            problemSubmission.setSubmissionStatus(SubmissionStatus.COMPLETED);
            submissionRepository.save(problemSubmission);

        } else {

            problemSubmission.setSubmissionStatus(SubmissionStatus.FAILED);
            submissionRepository.save(problemSubmission);
        }

        return problemResult;
    }
}
