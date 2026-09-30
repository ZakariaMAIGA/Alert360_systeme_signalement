package com.alert360.controller;

import com.alert360.controller.dto.APIResponse;
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
    public ResponseEntity<APIResponse<AuthResponseDto>> seConnecter(@Valid @RequestBody LoginRequestDto loginRequest) {
        AuthResponseDto response = authService.seConnecter(loginRequest);
        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Login successful",
                      response)
        );
    }

    @PostMapping("/register/citoyen")
    public ResponseEntity<APIResponse<AuthResponseDto>> inscrireCitoyen(@Valid @RequestBody CitoyenRequestDto request) {
        AuthResponseDto response = authService.inscrireCitoyen(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Citoyen créé avec succès.",
                        null));
    }
}