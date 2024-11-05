package com.tveu.jcode.code_service.core.kafka;

import com.tveu.jcode.code_service.api.dto.ProblemResultDTO;
import com.tveu.jcode.code_service.core.service.ProblemResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProblemResultEventConsumer implements KafkaEventConsumer<ProblemResultDTO> {

    private final ProblemResultService problemResultService;

    @KafkaListener(topics = KafkaTopic.PROBLEM_RESULT, groupId = "result-consumer-group", containerFactory = "kafkaProblemResultListenerContainerFactory")
    public void consume(ProblemResultDTO resultEvent) {
        log.info("Received ResultDTO event: {}", resultEvent);

        handleResultEvent(resultEvent);
    }

    private void handleResultEvent(ProblemResultDTO resultEvent) {

        log.info("Processing Result Event with ID: {}", resultEvent.resultDTO().id());
        log.debug("Submission id {}", resultEvent.resultDTO().submissionID());

        var problemResult = problemResultService.create(resultEvent);

        log.info("Finished processing Result Event with ID: {}", problemResult.getId());
    }
}
