package com.jay.qrCode_Service.controller.auth;

import com.jay.qrCode_Service.entity.UserEntity;
import com.jay.qrCode_Service.jwt.JwtUtil;
import com.jay.qrCode_Service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class OAuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @GetMapping("/oauth-success")
    public ResponseEntity<?> oauthSuccess(Authentication authentication) {

        if (authentication == null) {
            throw new RuntimeException("OAuth authentication failed (authentication is null)");
        }

        var principal = (org.springframework.security.oauth2.core.user.OAuth2User) authentication.getPrincipal();


        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String providerId = principal.getAttribute("sub");

        //Find or create user
        UserEntity user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    UserEntity u = UserEntity.builder()
                            .email(email)
                            .username(name)
                            .password("")
                            .roles("USER")
                            .provider("GOOGLE")
                            .createdAt(Instant.now())
                            .build();
                    return userRepository.save(u);
                });

        //generate Jwt token
        Map<String, Object> claims = Map.of(
                "username", user.getUsername(),
                "roles", user.getRoles(),
                "id", user.getId().toString()
        );

        String token = jwtUtil.generateToken(user.getEmail(), claims);

        // redirect to frontend with Jwt
        String redirectUrl = "http://localhost:4200/oauth-success?token=" + token;

        return ResponseEntity.status(302)
                .header("Location", redirectUrl)
                .build();
    }
}
