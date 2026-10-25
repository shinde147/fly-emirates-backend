package com.flyemirates.controller;

import com.flyemirates.dto.AuthResponse;
import com.flyemirates.dto.LoginRequest;
import com.flyemirates.dto.RegisterRequest;
import com.flyemirates.service.AuthService;
import jakarta.validation.Valid;   // NEW
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {   // NEW: @Valid
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {   // NEW: @Valid
        return ResponseEntity.ok(authService.login(request));
    }
}