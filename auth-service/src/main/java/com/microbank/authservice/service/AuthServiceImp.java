package com.microbank.authservice.services;


import com.microbank.authservice.auth.LoginRequest;
import com.microbank.authservice.auth.RegisterRequest;
import com.microbank.authservice.dtos.AuthResponse;
import com.microbank.authservice.dtos.RegisterRequestDTO;
import com.microbank.authservice.entities.Role;
import com.microbank.authservice.entities.User;
import com.microbank.authservice.kafka.UserCreatedEvent;
import com.microbank.authservice.kafka.UserCreatedProducer;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserCreatedProducer userCreatedProducer;

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

        return AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .expiresAt(jwtService.getExpirationFromToken(token))
                .build();
    }
}