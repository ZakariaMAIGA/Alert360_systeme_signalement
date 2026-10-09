package com.alert360.controller;

import com.alert360.controller.dto.APIResponse;
import com.alert360.controller.dto.CategorieRequestDto;
import com.alert360.controller.dto.CategorieResponseDto;
import com.alert360.service.serviceInter.CategorieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
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
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(
        name = "Catégories",
        description = "Gestion des catégories utilisées pour classer les signalements citoyens"
)
@SecurityRequirement(name = "bearerAuth")
public class CategorieController {

    private final CategorieService categorieService;


    // ============================================================
    // CREER UNE CATEGORIE
    // ============================================================

    @PostMapping
    @Operation(
            summary = "Créer une catégorie",
            description = "Permet de créer une nouvelle catégorie de signalement."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Catégorie créée avec succès"
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
                    description = "Une catégorie portant ce nom existe déjà"
            )
    })
    public ResponseEntity<APIResponse<Void>> creerCategorie(
            @Valid @RequestBody CategorieRequestDto dto
    ) {

        CategorieResponseDto response =
                categorieService.creerCategorie(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Categorie créé avec succès.",
                        null
                ));
    }


    // ============================================================
    // OBTENIR TOUTES LES CATEGORIES
    // ============================================================

    @GetMapping
    @Operation(
            summary = "Obtenir toutes les catégories",
            description = "Retourne la liste de toutes les catégories disponibles pour les signalements."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des catégories récupérée avec succès"
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
    public ResponseEntity<List<CategorieResponseDto>> obtenirToutesLesCategories() {

        List<CategorieResponseDto> categories =
                categorieService.obtenirToutesLesCategories();

        return ResponseEntity.ok(categories);
    }


    // ============================================================
    // OBTENIR UNE CATEGORIE PAR ID
    // ============================================================

    @GetMapping("/{idCategorie}")
    @Operation(
            summary = "Obtenir une catégorie par son ID",
            description = "Retourne les informations détaillées d'une catégorie à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Catégorie trouvée avec succès"
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
                    description = "Catégorie introuvable"
            )
    })
    public ResponseEntity<CategorieResponseDto> obtenirCategorieParId(

            @Parameter(
                    description = "Identifiant unique de la catégorie",
                    example = "1",
                    required = true
            )
            @PathVariable Long idCategorie
    ) {

        CategorieResponseDto categorie =
                categorieService.obtenirParId(idCategorie);

        return ResponseEntity.ok(categorie);
    }


    // ============================================================
    // MODIFIER UNE CATEGORIE
    // ============================================================

    @PutMapping("/{idCategorie}")
    @Operation(
            summary = "Modifier une catégorie",
            description = "Permet de modifier les informations d'une catégorie existante."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Catégorie modifiée avec succès"
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
                    description = "Catégorie introuvable"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Une catégorie portant ce nom existe déjà"
            )
    })
    public ResponseEntity<CategorieResponseDto> modifierCategorie(

            @Parameter(
                    description = "Identifiant unique de la catégorie à modifier",
                    example = "1",
                    required = true
            )
            @PathVariable Long idCategorie,

            @Valid @RequestBody CategorieRequestDto dto
    ) {

        CategorieResponseDto categorie =
                categorieService.modifierCategorie(idCategorie, dto);

        return ResponseEntity.ok(categorie);
    }


    // ============================================================
    // SUPPRIMER UNE CATEGORIE
    // ============================================================

    @DeleteMapping("/{idCategorie}")
    @Operation(
            summary = "Supprimer une catégorie",
            description = "Supprime une catégorie existante à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Catégorie supprimée avec succès"
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
                    description = "Catégorie introuvable"
            )
    })
    public ResponseEntity<Void> supprimerCategorie(

            @Parameter(
                    description = "Identifiant unique de la catégorie à supprimer",
                    example = "1",
                    required = true
            )
            @PathVariable Long idCategorie
    ) {

        categorieService.supprimerCategorie(idCategorie);

        return ResponseEntity.noContent().build();
    }
}