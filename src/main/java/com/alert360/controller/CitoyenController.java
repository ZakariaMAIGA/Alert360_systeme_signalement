package com.alert360.controller;

import com.alert360.controller.dto.APIResponse;
import com.alert360.controller.dto.CitoyenRequestDto;
import com.alert360.controller.dto.CitoyenResponseDto;
import com.alert360.service.serviceInter.CitoyenService;
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
@RequestMapping("/api/citoyens")
@RequiredArgsConstructor
@Tag(
        name = "Citoyens",
        description = "Gestion des comptes citoyens et de leurs informations civiques"
)
@SecurityRequirement(name = "bearerAuth")
public class CitoyenController {

    private final CitoyenService citoyenService;


    // ============================================================
    // CREER UN CITOYEN
    // ============================================================

    @PostMapping
    @Operation(
            summary = "Créer un citoyen",
            description = "Permet de créer un nouveau compte citoyen."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Citoyen créé avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides"
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
                    responseCode = "409",
                    description = "Un utilisateur avec ces informations existe déjà"
            )
    })
    public ResponseEntity<APIResponse<Void>> creerCitoyen(
            @Valid @RequestBody CitoyenRequestDto dto
    ) {

        CitoyenResponseDto nouveauCitoyen =
                citoyenService.creerCitoyen(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Citoyen créé avec succès.",
                        null
                ));
    }


    // ============================================================
    // MODIFIER UN CITOYEN
    // ============================================================

    @PutMapping("/{idUtilisateur}")
    @Operation(
            summary = "Modifier un citoyen",
            description = "Permet de modifier les informations d'un citoyen existant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Citoyen modifié avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides"
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
                    description = "Citoyen introuvable"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Les informations fournies sont déjà utilisées"
            )
    })
    public ResponseEntity<APIResponse<Object>> modifierCitoyen(

            @Parameter(
                    description = "Identifiant unique du citoyen",
                    example = "1",
                    required = true
            )
            @PathVariable Long idUtilisateur,

            @Valid @RequestBody CitoyenRequestDto dto
    ) {

        CitoyenResponseDto citoyenModifie =
                citoyenService.modifierCitoyen(idUtilisateur, dto);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Citoyen modifié avec succès.",
                        citoyenModifie
                )
        );
    }


    // ============================================================
    // OBTENIR UN CITOYEN PAR ID
    // ============================================================

    @GetMapping("/{idUtilisateur}")
    @Operation(
            summary = "Obtenir un citoyen par son ID",
            description = "Retourne les informations d'un citoyen à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Citoyen trouvé avec succès"
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
                    description = "Citoyen introuvable"
            )
    })
    public ResponseEntity<APIResponse<CitoyenResponseDto>> obtenirParId(

            @Parameter(
                    description = "Identifiant unique du citoyen",
                    example = "1",
                    required = true
            )
            @PathVariable Long idUtilisateur
    ) {

        CitoyenResponseDto citoyen =
                citoyenService.obtenirParId(idUtilisateur);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Citoyen trouvé avec succès.",
                        citoyen
                )
        );
    }


    // ============================================================
    // OBTENIR TOUS LES CITOYENS
    // ============================================================

    @GetMapping
    @Operation(
            summary = "Obtenir tous les citoyens",
            description = "Retourne la liste de tous les citoyens enregistrés dans le système."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des citoyens récupérée avec succès"
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
    public ResponseEntity<APIResponse<List<CitoyenResponseDto>>> obtenirTousLesCitoyens() {

        List<CitoyenResponseDto> citoyens =
                citoyenService.obtenirTousLesCitoyens();

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Les citoyens ont été récupérés avec succès.",
                        citoyens
                )
        );
    }


    // ============================================================
    // OBTENIR LES CITOYENS PAR QUARTIER
    // ============================================================

    @GetMapping("/quartier/{quartier}")
    @Operation(
            summary = "Obtenir les citoyens d'un quartier",
            description = "Retourne la liste des citoyens appartenant à un quartier donné."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Citoyens du quartier récupérés avec succès"
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
    public ResponseEntity<APIResponse<List<CitoyenResponseDto>>> obtenirParQuartier(

            @Parameter(
                    description = "Nom du quartier recherché",
                    example = "Hamdallaye",
                    required = true
            )
            @PathVariable String quartier
    ) {

        List<CitoyenResponseDto> citoyens =
                citoyenService.obtenirParQuartier(quartier);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Les citoyens du quartier ont été récupérés avec succès.",
                        citoyens
                )
        );
    }


    // ============================================================
    // AJOUTER UN BADGE CIVIQUE
    // ============================================================

    @PatchMapping("/{idUtilisateur}/badges")
    @Operation(
            summary = "Ajouter un badge civique",
            description = "Permet d'ajouter un badge civique au profil d'un citoyen."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Badge civique ajouté avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Badge invalide ou paramètre manquant"
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
                    description = "Citoyen introuvable"
            )
    })
    public ResponseEntity<APIResponse<CitoyenResponseDto>> ajouterBadgeCivique(

            @Parameter(
                    description = "Identifiant unique du citoyen",
                    example = "1",
                    required = true
            )
            @PathVariable Long idUtilisateur,

            @Parameter(
                    description = "Nom du badge civique à ajouter",
                    example = "Citoyen engagé",
                    required = true
            )
            @RequestParam String badge
    ) {

        CitoyenResponseDto citoyenAjourne =
                citoyenService.ajouterBadgeCivique(idUtilisateur, badge);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Le badge civique a été ajouté avec succès.",
                        citoyenAjourne
                )
        );
    }


    // ============================================================
    // SUPPRIMER UN CITOYEN
    // ============================================================

    @DeleteMapping("/{idUtilisateur}")
    @Operation(
            summary = "Supprimer un citoyen",
            description = "Supprime le compte d'un citoyen à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Citoyen supprimé avec succès"
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
                    description = "Citoyen introuvable"
            )
    })
    public ResponseEntity<APIResponse<Void>> supprimerCitoyen(

            @Parameter(
                    description = "Identifiant unique du citoyen à supprimer",
                    example = "1",
                    required = true
            )
            @PathVariable Long idUtilisateur
    ) {

        citoyenService.supprimerCitoyen(idUtilisateur);

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Le citoyen a été supprimé avec succès.",
                        null
                )
        );
    }
}