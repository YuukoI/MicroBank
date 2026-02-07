package com.microbank.authservice.kafka;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreatedProducer {


    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObservationRegistry observationRegistry;

    private static final String TOPIC = "user-created-topic";

    public void sendUserCreated(UserCreatedEvent event) {

        Observation.createNotStarted("kafka.producer.user-created", observationRegistry)
                .observe(() ->
                        kafkaTemplate.send(
                                TOPIC,
                                event.getUsername(),
                                event
                        )
                );
    }
}