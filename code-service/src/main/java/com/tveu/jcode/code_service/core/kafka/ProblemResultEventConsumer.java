package com.tveu.jcode.code_service.core.kafka;

import com.tveu.jcode.code_service.api.dto.ProblemResultDTO;
import com.tveu.jcode.code_service.core.mapper.TestResultMapper;
import com.tveu.jcode.code_service.core.repository.TestResultRepository;
import com.tveu.jcode.code_service.core.service.ResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProblemResultEventConsumer implements KafkaEventConsumer<ProblemResultDTO> {

    private final ResultService resultService;
    private final TestResultRepository testResultRepository;
    private final TestResultMapper testResultMapper;

    @KafkaListener(topics = KafkaTopic.PROBLEM_RESULT, groupId = "result-consumer-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ProblemResultDTO resultEvent) {
        log.info("Received ResultDTO event: {}", resultEvent);

        handleResultEvent(resultEvent);
    }

    private void handleResultEvent(ProblemResultDTO resultEvent) {

        log.info("Processing Result Event with ID: {}", resultEvent.resultDTO().id());
        log.debug("Submission id {}", resultEvent.resultDTO().submissionID());

        log.info("saving test results");
        testResultRepository.saveAll(testResultMapper.map(resultEvent.testResults()));

        log.info("saving resultDTO");
        resultService.create(resultEvent.resultDTO());


        log.info("Finished processing Result Event with ID: {}", resultEvent.resultDTO().id());

    }
}
