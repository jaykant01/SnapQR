package com.jay.qrCode_Service.service;

import com.jay.qrCode_Service.dto.auth.LoginRequest;
import com.jay.qrCode_Service.dto.auth.RegisterRequest;
import com.jay.qrCode_Service.entity.UserEntity;
import com.jay.qrCode_Service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String testService() {
        long count = userRepository.count();
        return "User in DB: " + count;
    }


    // Register
    public String registerUser(RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return "Email already registered";
        }

        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            return "Username already taken";
        }

        UserEntity newUser = UserEntity.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .roles("USER")
                .provider("local")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        userRepository.save(newUser);

        return "User registered successfully";
    }

    // Login
    public String loginUser(LoginRequest req) {

        UserEntity user = userRepository.findByEmail(req.getEmail())
                .orElse(null);

        if (user == null) {
            return "Invalid email or password";
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return "Invalid email or password";
        }

        return "Login successful";
    }

}
