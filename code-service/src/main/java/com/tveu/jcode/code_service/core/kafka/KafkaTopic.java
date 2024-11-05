package com.tveu.jcode.code_service.core.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopic {

    public static final String SUBMISSION = "submissions";

    public static final String PROBLEM_SUBMISSION = "problem_submissions";

    public static final String PROBLEM_RESULT = "problem";

    @Bean
    public NewTopic submissionsTopic() {
        return TopicBuilder.name(SUBMISSION).build();
    }

    @Bean
    public NewTopic problemSubmissionsTopic() {
        return TopicBuilder.name(PROBLEM_SUBMISSION).build();
    }
}
