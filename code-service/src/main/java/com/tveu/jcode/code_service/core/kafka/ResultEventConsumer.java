package com.tveu.jcode.code_service.core.kafka;

import com.tveu.jcode.code_service.api.dto.ResultDTO;
import com.tveu.jcode.code_service.core.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultEventConsumer {

    private final ResultService resultService;

    private static final Logger logger = LoggerFactory.getLogger(ResultEventConsumer.class);

    @KafkaListener(topics = "result", groupId = "result-consumer-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ResultDTO resultEvent) {
        logger.info("Received ResultDTO event: {}", resultEvent);

        handleResultEvent(resultEvent);
    }

    private void handleResultEvent(ResultDTO resultEvent) {

        logger.info("Processing Result Event with ID: {}", resultEvent.id());
        logger.debug("Submission id {}", resultEvent.submissionID());

        resultService.create(resultEvent);
        logger.info("Finished processing Result Event with ID: {}", resultEvent.id());

    }
}
