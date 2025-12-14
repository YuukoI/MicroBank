package com.microbank.authservice.services;

import com.microbank.authservice.auth.LoginRequest;
import com.microbank.authservice.auth.RegisterRequest;
import com.microbank.authservice.dtos.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);

    AuthResponse register(RegisterRequest registerRequest);

}

