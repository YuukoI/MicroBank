package com.microbank.authservice.config;

import com.microbank.authservice.kafka.UserCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, UserCreatedEvent> kafkaTemplate(
            ProducerFactory<String, UserCreatedEvent> pf) {
        return new KafkaTemplate<>(pf);
    }

}