package com.alert360.controller;

import com.alert360.controller.dto.UtilisateurResponseDto;
import com.alert360.service.serviceInter.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
@Tag(
        name = "Utilisateurs",
        description = "Consultation des informations des utilisateurs de la plateforme Alert'360"
)
@SecurityRequirement(name = "bearerAuth")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    // =========================
    // OBTENIR UN UTILISATEUR PAR ID
    // =========================
    @GetMapping("/{idUtilisateur}")
    @Operation(
            summary = "Obtenir un utilisateur par son identifiant",
            description = "Retourne les informations d'un utilisateur à partir de son identifiant unique."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur trouvé avec succès"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès interdit"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            )
    })
    public ResponseEntity<UtilisateurResponseDto> obtenirParId(

            @Parameter(
                    description = "Identifiant unique de l'utilisateur",
                    example = "1",
                    required = true
            )
            @PathVariable Long idUtilisateur
    ) {
        UtilisateurResponseDto utilisateur =
                utilisateurService.obtenirParId(idUtilisateur);

        return ResponseEntity.ok(utilisateur);
    }

    // =========================
    // OBTENIR UN UTILISATEUR PAR EMAIL
    // =========================
    @GetMapping("/email")
    @Operation(
            summary = "Obtenir un utilisateur par son email",
            description = "Recherche et retourne un utilisateur à partir de son adresse email."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur trouvé avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Adresse email invalide ou manquante"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès interdit"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Aucun utilisateur trouvé avec cet email"
            )
    })
    public ResponseEntity<UtilisateurResponseDto> obtenirParEmail(

            @Parameter(
                    description = "Adresse email de l'utilisateur recherché",
                    example = "utilisateur@gmail.com",
                    required = true
            )
            @RequestParam String email
    ) {
        UtilisateurResponseDto utilisateur =
                utilisateurService.obtenirParEmail(email);

        return ResponseEntity.ok(utilisateur);
    }

    // =========================
    // OBTENIR TOUS LES UTILISATEURS
    // =========================
    @GetMapping
    @Operation(
            summary = "Obtenir tous les utilisateurs",
            description = "Retourne la liste de tous les utilisateurs enregistrés sur la plateforme Alert'360."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des utilisateurs récupérée avec succès"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès interdit"
            )
    })
    public ResponseEntity<List<UtilisateurResponseDto>> obtenirTousLesUtilisateurs() {

        List<UtilisateurResponseDto> utilisateurs =
                utilisateurService.obtenirTousLesUtilisateurs();

        return ResponseEntity.ok(utilisateurs);
    }
}