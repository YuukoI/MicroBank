package com.microbank.authservice.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreatedProducer {

    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    public void sendUserCreated(UserCreatedEvent event) {
        kafkaTemplate.send("user-created-topic", event);
    }
}