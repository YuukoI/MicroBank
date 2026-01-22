package com.microbank.authservice.service;

import com.microbank.authservice.auth.LoginRequest;
import com.microbank.authservice.dto.AuthResponse;
import com.microbank.authservice.dto.RegisterRequestDTO;

public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);

    AuthResponse register(RegisterRequestDTO registerRequestDTO);

}

