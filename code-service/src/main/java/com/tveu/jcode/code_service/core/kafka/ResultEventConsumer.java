package com.tveu.jcode.code_service.core.kafka;

import com.tveu.jcode.code_service.api.dto.ResultDTO;
import com.tveu.jcode.code_service.core.service.ResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResultEventConsumer implements KafkaEventConsumer<ResultDTO>{

    private final ResultService resultService;

    @KafkaListener(topics = "result", groupId = "result-consumer-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ResultDTO resultEvent) {
        log.info("Received ResultDTO event: {}", resultEvent);

        handleResultEvent(resultEvent);
    }

    private void handleResultEvent(ResultDTO resultEvent) {

        log.info("Processing Result Event with ID: {}", resultEvent.id());
        log.debug("Submission id {}", resultEvent.submissionID());

        resultService.create(resultEvent);
        log.info("Finished processing Result Event with ID: {}", resultEvent.id());

    }
}
