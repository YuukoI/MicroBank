package com.microbank.userservice.kafka;

import com.microbank.userservice.entity.Role;
import com.microbank.userservice.entity.User;
import com.microbank.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserCreatedConsumer {

    private final UserRepository userRepository;

    @Transactional
    @KafkaListener(topics = "user-created-topic", groupId = "user-service-group")
    public void consume(UserCreatedEvent event) {

        System.out.println("📥 event received in USER-SERVICE: " + event);

        if (event == null) {
            System.out.println("⚠️ Null, just ignore");
            return;
        }

        if (userRepository.existsByUsername(event.getUsername())) {
            System.out.println("ℹ️ User already exists: " + event.getUsername());
            return;
        }

        User user = User.builder()
                .username(event.getUsername())
                .firstname(event.getFirstname())
                .lastname(event.getLastname())
                .country(event.getCountry())
                .role(Role.valueOf(event.getRole()))
                .build();

        userRepository.save(user);

        System.out.println("✅ User saved: " + user.getUsername());
    }
}