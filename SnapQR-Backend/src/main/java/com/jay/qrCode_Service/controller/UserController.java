package com.jay.qrCode_Service.controller;

import com.jay.qrCode_Service.entity.UserEntity;
import com.jay.qrCode_Service.repository.UserRepository;
import com.jay.qrCode_Service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin
@RequiredArgsConstructor
//used this annotation instead of autowired (field injection) it is constructor injection used with final keyword
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/test")
    public String test() {
        return userService.testService();
    }

    @GetMapping("/me")
    public UserEntity getMe(Authentication authentication) {
        String email = authentication.getName();
        //System.out.println("=== AUTH NAME: " + authentication.getName());
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
