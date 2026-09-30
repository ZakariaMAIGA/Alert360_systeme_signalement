package com.alert360.controller;

import com.alert360.controller.dto.AuthResponseDto;
import com.alert360.controller.dto.CitoyenRequestDto;
import com.alert360.controller.dto.LoginRequestDto;
import com.alert360.service.serviceImpl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> seConnecter(@Valid @RequestBody LoginRequestDto loginRequest) {
        AuthResponseDto response = authService.seConnecter(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/citoyen")
    public ResponseEntity<AuthResponseDto> inscrireCitoyen(@Valid @RequestBody CitoyenRequestDto request) {
        AuthResponseDto response = authService.inscrireCitoyen(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}