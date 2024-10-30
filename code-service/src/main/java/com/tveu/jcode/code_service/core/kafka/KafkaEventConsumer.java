package com.tveu.jcode.code_service.core.kafka;

public interface KafkaEventConsumer<T> {

    void consume(T event);
}