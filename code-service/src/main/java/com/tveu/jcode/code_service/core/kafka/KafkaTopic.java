package com.tveu.jcode.code_service.core.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {

    // Topic for general submissions
    public static final String SUBMISSION_TOPIC = "submissions";

    // Topic for problem-specific submissions
    public static final String PROBLEM_SUBMISSION_TOPIC = "problem_submissions";

    @Bean
    public NewTopic submissionsTopic() {
        return TopicBuilder.name(SUBMISSION_TOPIC).build();
    }

    @Bean
    public NewTopic problemSubmissionsTopic() {
        return TopicBuilder.name(PROBLEM_SUBMISSION_TOPIC).build();
    }
}
