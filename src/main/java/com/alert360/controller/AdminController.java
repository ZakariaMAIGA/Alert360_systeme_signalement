 package com.alert360.controller;

import com.alert360.controller.dto.AdminUtilisateurCreateDto;
import com.alert360.controller.dto.AdminUtilisateurUpdateDto;
import com.alert360.controller.dto.UtilisateurResponseDto;
import com.alert360.service.serviceInter.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(
        name = "Administration",
        description = "Gestion des utilisateurs et des comptes par les administrateurs"
)
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AdminService adminService;


    // =========================================================
    // OBTENIR UN UTILISATEUR PAR ID
    // =========================================================

    @GetMapping("/utilisateurs/{idUtilisateur}")
    @Operation(
            summary = "Consulter un utilisateur",
            description = "Récupère les informations d'un utilisateur à partir de son identifiant."
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
                    description = "Accès refusé : droits administrateur requis"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            )
    })
    public ResponseEntity<UtilisateurResponseDto> obtenirUtilisateurParId(

            @Parameter(
                    description = "Identifiant unique de l'utilisateur",
                    required = true,
                    example = "1"
            )
            @PathVariable Long idUtilisateur
    ) {

        return ResponseEntity.ok(
                adminService.obtenirUtilisateurParId(idUtilisateur)
        );
    }


    // =========================================================
    // OBTENIR TOUS LES UTILISATEURS
    // =========================================================

    @GetMapping("/utilisateurs")
    @Operation(
            summary = "Lister les utilisateurs",
            description = "Récupère la liste de tous les utilisateurs enregistrés sur la plateforme."
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
                    description = "Accès refusé : droits administrateur requis"
            )
    })
    public ResponseEntity<List<UtilisateurResponseDto>>
    obtenirTousLesUtilisateurs() {

        return ResponseEntity.ok(
                adminService.obtenirTousLesUtilisateurs()
        );
    }


    // =========================================================
    // CREER UN UTILISATEUR
    // =========================================================

    @PostMapping("/utilisateurs")
    @Operation(
            summary = "Créer un utilisateur",
            description = "Permet à un administrateur de créer un nouveau compte utilisateur."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Utilisateur créé avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de création invalides"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé : droits administrateur requis"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Un utilisateur avec ces informations existe déjà"
            )
    })
    public ResponseEntity<UtilisateurResponseDto> creerUtilisateur(
            @Valid @RequestBody AdminUtilisateurCreateDto dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(adminService.creerUtilisateur(dto));
    }


    // =========================================================
    // MODIFIER UN UTILISATEUR
    // =========================================================

    @PutMapping("/utilisateurs/{idUtilisateur}")
    @Operation(
            summary = "Modifier un utilisateur",
            description = "Permet à un administrateur de modifier les informations d'un utilisateur existant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur modifié avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de modification invalides"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé : droits administrateur requis"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Les informations fournies sont déjà utilisées par un autre utilisateur"
            )
    })
    public ResponseEntity<UtilisateurResponseDto> modifierUtilisateur(

            @Parameter(
                    description = "Identifiant unique de l'utilisateur à modifier",
                    required = true,
                    example = "1"
            )
            @PathVariable Long idUtilisateur,

            @Valid @RequestBody AdminUtilisateurUpdateDto dto
    ) {

        return ResponseEntity.ok(
                adminService.modifierUtilisateur(
                        idUtilisateur,
                        dto
                )
        );
    }


    // =========================================================
    // CHANGER LE STATUT D'UN COMPTE
    // =========================================================

    @PatchMapping("/utilisateurs/{idUtilisateur}/statut")
    @Operation(
            summary = "Modifier le statut d'un compte",
            description = "Permet à un administrateur d'activer ou de désactiver le compte d'un utilisateur."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Statut du compte modifié avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Valeur du statut invalide"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé : droits administrateur requis"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            )
    })
    public ResponseEntity<UtilisateurResponseDto> changerStatutCompte(

            @Parameter(
                    description = "Identifiant unique de l'utilisateur",
                    required = true,
                    example = "1"
            )
            @PathVariable Long idUtilisateur,

            @Parameter(
                    description = "Nouveau statut du compte : true pour activer, false pour désactiver",
                    required = true,
                    example = "true"
            )
            @RequestParam Boolean estActif
    ) {

        return ResponseEntity.ok(
                adminService.changerStatutCompte(
                        idUtilisateur,
                        estActif
                )
        );
    }
}