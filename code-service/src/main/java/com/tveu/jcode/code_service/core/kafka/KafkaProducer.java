package com.tveu.jcode.code_service.core.kafka;

import com.tveu.jcode.code_service.api.dto.SubmissionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, SubmissionDTO> submissionKafkaTemplate;  // Constructor injection

    public void sendMessage(SubmissionDTO message, String topicName) {
        log.info("Sending : {}", message);
        log.info("--------------------------------");

        submissionKafkaTemplate.send(topicName, message);
    }
}
