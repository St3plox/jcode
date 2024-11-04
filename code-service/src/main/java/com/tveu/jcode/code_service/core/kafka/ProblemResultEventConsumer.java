package com.tveu.jcode.code_service.core.kafka;

import com.tveu.jcode.code_service.api.dto.ProblemResultDTO;
import com.tveu.jcode.code_service.core.entity.ProblemResult;
import com.tveu.jcode.code_service.core.entity.SubmissionStatus;
import com.tveu.jcode.code_service.core.mapper.TestResultMapper;
import com.tveu.jcode.code_service.core.repository.ProblemResultRepository;
import com.tveu.jcode.code_service.core.repository.ProblemSubmissionRepository;
import com.tveu.jcode.code_service.core.repository.TestResultRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProblemResultEventConsumer implements KafkaEventConsumer<ProblemResultDTO> {

    private final ProblemResultRepository problemResultRepository;
    private final TestResultRepository testResultRepository;
    private final TestResultMapper testResultMapper;
    private final ProblemSubmissionRepository submissionRepository;

    @KafkaListener(topics = KafkaTopic.PROBLEM_RESULT, groupId = "result-consumer-group", containerFactory = "kafkaProblemResultListenerContainerFactory")
    public void consume(ProblemResultDTO resultEvent) {
        log.info("Received ResultDTO event: {}", resultEvent);

        handleResultEvent(resultEvent);
    }

    //TODO: solve   Detail: Key (submission_id)=(508226f8-ac63-42a0-8600-0f87ec7ac8bd) is not present in tab
    // le "submissions".] [insert into results (created_at,errors,output,submission_id,id) values (?,?,?,?,?)]; 
    // SQL [insert into results (created_at,errors,output,submission_id,id) values (?,?,?,?,?)]; constraint [fk_submission]
    //TODO: create new entity for problem_results
    private void handleResultEvent(ProblemResultDTO resultEvent) {

        log.info("Processing Result Event with ID: {}", resultEvent.resultDTO().id());
        log.debug("Submission id {}", resultEvent.resultDTO().submissionID());

        ProblemResult problemResult = new ProblemResult(
                UUID.fromString(resultEvent.resultDTO().id()),
                null,
                resultEvent.resultDTO().output(),
                resultEvent.resultDTO().errors(),
                null);

        log.info("saving test results");
        testResultRepository.saveAll(testResultMapper.map(resultEvent.testResults()));

        var problemSubmission = submissionRepository.findById(UUID.fromString(resultEvent.resultDTO().submissionID()))
                        .orElseThrow(() -> new RuntimeException("Submission not found"));

        log.info("saving resultDTO");
        problemResult.setSubmission(problemSubmission);
        problemResultRepository.save(problemResult);

        if (resultEvent.resultDTO().errors().isEmpty()) {

            problemSubmission.setSubmissionStatus(SubmissionStatus.COMPLETED);
            submissionRepository.save(problemSubmission);

        } else {

            problemSubmission.setSubmissionStatus(SubmissionStatus.FAILED);
            submissionRepository.save(problemSubmission);
        }

        
        log.info("Finished processing Result Event with ID: {}", resultEvent.resultDTO().id());
    }
}
