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
        description = "Gestion de l'authentification et de l'inscription des citoyens"
)
public class AuthController {

    private final AuthService authService;


    // =========================================================
    // CONNEXION
    // =========================================================

    @PostMapping("/login")
    @Operation(
            summary = "Se connecter",
            description = "Authentifie un utilisateur à partir de ses identifiants et retourne un token JWT ainsi que les informations de son compte."
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
                    description = "Identifiants incorrects ou compte non autorisé"
            )
    })
    public ResponseEntity<APIResponse<AuthResponseDto>> seConnecter(

            @Valid
            @RequestBody
            LoginRequestDto loginRequest

    ) {

        AuthResponseDto response =
                authService.seConnecter(loginRequest);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Login successful",
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

            @Valid
            @RequestBody
            CitoyenRequestDto request

    ) {

        AuthResponseDto response =
                authService.inscrireCitoyen(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new APIResponse<>(
                                true,
                                "Citoyen créé avec succès.",
                                null
                        )
                );
    }
}
