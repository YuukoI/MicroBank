package com.microbank.userservice.kafka;

import com.microbank.userservice.entities.Role;
import com.microbank.userservice.entities.User;
import com.microbank.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserCreatedConsumer {

    private final UserRepository userRepository;

    @KafkaListener(
            topics = "user-created-topic",
            groupId = "user-service-group"
    )
    public void consume(UserCreatedEvent event) {

        if (userRepository.existsByUsername(event.getUsername())) {
            return;
        }

        User user = User.builder()
                .username(event.getUsername())
                .password(event.getPassword())
                .firstname(event.getFirstname())
                .lastname(event.getLastname())
                .country(event.getCountry())
                .role(Role.valueOf(event.getRole()))
                .build();

        userRepository.save(user);
    }
}