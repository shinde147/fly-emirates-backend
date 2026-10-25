package com.flyemirates.service;

import com.flyemirates.dto.AuthResponse;
import com.flyemirates.dto.LoginRequest;
import com.flyemirates.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
