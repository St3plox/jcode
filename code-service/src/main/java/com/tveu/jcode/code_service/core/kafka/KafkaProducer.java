package com.tveu.jcode.code_service.core.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> void sendMessage(T message, String topicName) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            log.info("Sending to topic {}: {}", topicName, jsonMessage);
            log.info("--------------------------------");

            kafkaTemplate.send(topicName, jsonMessage);  // Send the JSON string
        } catch (Exception e) {
            log.error("Failed to send message to {}: {}", topicName, e.getMessage(), e);
        }
    }
}
