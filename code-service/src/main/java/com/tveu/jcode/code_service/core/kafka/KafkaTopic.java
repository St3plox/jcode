package com.tveu.jcode.code_service.core.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {

    public static final String SUBMISSION_TOPIC = "submissions";

    @Bean
    public NewTopic submissionsTopic() {
        return TopicBuilder.name(SUBMISSION_TOPIC).build();
    }

}