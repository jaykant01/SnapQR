package com.jay.qrCode_Service.controller.auth;

import com.jay.qrCode_Service.dto.auth.AuthResponse;
import com.jay.qrCode_Service.dto.auth.LoginRequest;
import com.jay.qrCode_Service.dto.auth.RegisterRequest;
import com.jay.qrCode_Service.service.AuthService;
import com.jay.qrCode_Service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        AuthResponse resp = authService.authenticateAndGetToken(request);
        return ResponseEntity.ok(resp);
    }
}
