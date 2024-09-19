package com.tveu.code_exec_worker.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tveu.code_exec_worker.dto.SubmissionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSubmissionConsumer {

    private final ObjectMapper objectMapper;  // Inject ObjectMapper to handle JSON deserialization

    @KafkaListener(topics = "submissions", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeSubmission(String message) throws Exception {

        // Deserialize the JSON string into a Map
        var submissionMap = objectMapper.readValue(message, Map.class);

        // Convert the Map into SubmissionDTO
        SubmissionDTO submissionDTO = objectMapper.convertValue(submissionMap, SubmissionDTO.class);

        // Process the submissionDTO
        log.info("Received and processed submission: {}", submissionDTO);
    }
}
