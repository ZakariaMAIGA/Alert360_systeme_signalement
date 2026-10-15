
        package com.alert360.controller;

import com.alert360.controller.dto.APIResponse;
import com.alert360.controller.dto.AuthResponseDto;
import com.alert360.controller.dto.CitoyenRequestDto;
import com.alert360.controller.dto.LoginRequestDto;
import com.alert360.service.serviceImpl.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(
        name = "Authentification",
        description = "Gestion de l'authentification web et mobile ainsi que de l'inscription des citoyens."
)
public class AuthController {

    private final AuthService authService;

    // =========================================================
    // CONNEXION MOBILE
    // =========================================================

    @PostMapping("/login")
    @Operation(
            summary = "Connexion mobile",
            description = "Authentifie un citoyen ou un agent de terrain et retourne un token JWT ainsi que les informations du compte."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentification réussie"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de connexion invalides"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Identifiants incorrects ou profil non autorisé sur mobile"
            )
    })
    public ResponseEntity<APIResponse<AuthResponseDto>> seConnecter(
            @Valid @RequestBody LoginRequestDto loginRequest
    ) {

        AuthResponseDto response =
                authService.seConnecter(loginRequest);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Connexion mobile réussie",
                        response
                )
        );
    }

    // =========================================================
    // CONNEXION WEB
    // =========================================================

    @PostMapping("/login/web")
    @Operation(
            summary = "Connexion web",
            description = "Authentifie un administrateur ou un responsable de structure et retourne un token JWT ainsi que les informations du compte."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentification web réussie"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de connexion invalides"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Identifiants incorrects ou profil non autorisé sur le web"
            )
    })
    public ResponseEntity<APIResponse<AuthResponseDto>> seConnecterWeb(
            @Valid @RequestBody LoginRequestDto loginRequest
    ) {

        AuthResponseDto response =
                authService.seConnecterWeb(loginRequest);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Connexion web réussie",
                        response
                )
        );
    }

    // =========================================================
    // INSCRIPTION CITOYEN
    // =========================================================

    @PostMapping("/register/citoyen")
    @Operation(
            summary = "Inscrire un citoyen",
            description = "Permet à un nouveau citoyen de créer un compte sur la plateforme Alert'360."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Compte citoyen créé avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données d'inscription invalides"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Un compte existe déjà avec les informations fournies"
            )
    })
    public ResponseEntity<APIResponse<AuthResponseDto>> inscrireCitoyen(
            @Valid @RequestBody CitoyenRequestDto request
    ) {

        AuthResponseDto response =
                authService.inscrireCitoyen(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new APIResponse<>(
                                true,
                                "Citoyen créé avec succès.",
                                response
                        )
                );
    }
}
