package com.jay.qrCode_Service.service;

import com.jay.qrCode_Service.dto.auth.AuthResponse;
import com.jay.qrCode_Service.dto.auth.LoginRequest;
import com.jay.qrCode_Service.entity.UserEntity;
import com.jay.qrCode_Service.jwt.JwtUtil;
import com.jay.qrCode_Service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthResponse authenticateAndGetToken(LoginRequest req) {
        //1st authenticate credentials
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword());

        authenticationManager.authenticate(authToken);

        // 2nd Load user entity to include claims
        UserEntity user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("roles", user.getRoles());
        claims.put("id", user.getId().toString());

        String token = jwtUtil.generateToken(req.getEmail(), claims);

        // expiration value is configured in JwtUtil constructor via property
        return new AuthResponse(token, jwtUtil.extractAllClaims(token).getExpiration().getTime());
    }

}
