package com.microbank.authservice.service;

import com.microbank.authservice.auth.LoginRequest;
import com.microbank.authservice.dto.AuditMessage;
import com.microbank.authservice.dto.AuthResponse;
import com.microbank.authservice.dto.RegisterRequestDTO;
import com.microbank.authservice.entity.Role;
import com.microbank.authservice.entity.User;
import com.microbank.authservice.kafka.UserCreatedEvent;
import com.microbank.authservice.kafka.UserCreatedProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import io.micrometer.tracing.Tracer;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserCreatedProducer userCreatedProducer;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Tracer tracer;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        User user = userService.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found."));

        String token = jwtService.getToken(user);

        sendAudit("LOGIN", loginRequest.getUsername(), "Login successful");

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .expiresAt(jwtService.getExpirationFromToken(token))
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequestDTO request) {

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userService.saveUser(user);

        userCreatedProducer.sendUserCreated(
                new UserCreatedEvent(
                        request.getUsername(),
                        request.getFirstname(),
                        request.getLastname(),
                        request.getCountry(),
                        Role.USER.name()
                )
        );

        String token = jwtService.getToken(user);

        sendAudit("REGISTER", request.getUsername(), "User registered");

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .expiresAt(jwtService.getExpirationFromToken(token))
                .build();
    }

    private void sendAudit(String action, String username, String details) {
        String traceId = (tracer.currentSpan() != null)
                ? tracer.currentSpan().context().traceId()
                : "N/A";

        AuditMessage audit = AuditMessage.builder()
                .serviceName("auth-service")
                .action(action)
                .username(username)
                .details(details)
                .traceId(traceId)
                .timestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send("audit-topic", audit);
    }
}