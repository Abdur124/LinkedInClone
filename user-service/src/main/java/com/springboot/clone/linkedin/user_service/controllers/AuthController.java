package com.springboot.clone.linkedin.user_service.controllers;

import com.springboot.clone.linkedin.user_service.config.AppConfig;
import com.springboot.clone.linkedin.user_service.dto.LoginRequestDto;
import com.springboot.clone.linkedin.user_service.dto.SignUpRequestDto;
import com.springboot.clone.linkedin.user_service.dto.UserDto;
import com.springboot.clone.linkedin.user_service.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
           UserDto userDto = authService.signUp(signUpRequestDto);
           return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        String accessToken = authService.login(loginRequestDto);
        log.info("Access value: {}", appConfig.getAccessVariable());
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @GetMapping("/{userId}/getUserName")
    public ResponseEntity<String> getUserName(@PathVariable Long userId) {
        String username = authService.getUsername(userId);
        return new ResponseEntity<>(username, HttpStatus.OK);
    }

    @GetMapping("/{userId}/getUserEmail")
    public ResponseEntity<String> getUserEmail(@PathVariable Long userId) {
        String userMail = authService.getUserEmail(userId);
        return new ResponseEntity<>(userMail, HttpStatus.OK);
    }
}
