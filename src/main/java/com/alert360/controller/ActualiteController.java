package com.alert360.controller;

import com.alert360.controller.dto.ActualiteRequestDto;
import com.alert360.controller.dto.ActualiteResponseDto;
import com.alert360.service.serviceInter.ActualiteService;
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
@RequestMapping("/api/actualites")
@RequiredArgsConstructor
@Tag(
        name = "Actualités",
        description = "Gestion des actualités publiées par les administrateurs"
)
@SecurityRequirement(name = "bearerAuth")
public class ActualiteController {

    private final ActualiteService actualiteService;


    // =========================================================
    // PUBLIER UNE ACTUALITE
    // =========================================================

    @PostMapping
    @Operation(
            summary = "Publier une actualité",
            description = "Permet à un administrateur connecté de publier une nouvelle actualité. " +
                    "L'auteur est automatiquement identifié à partir de l'utilisateur authentifié."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Actualité publiée avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de l'actualité invalides"
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
    public ResponseEntity<ActualiteResponseDto> publierActualite(
            @Valid @RequestBody ActualiteRequestDto dto
    ) {

        ActualiteResponseDto response =
                actualiteService.publierActualite(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // OBTENIR TOUTES LES ACTUALITES
    // =========================================================

    @GetMapping
    @Operation(
            summary = "Lister les actualités",
            description = "Récupère la liste de toutes les actualités publiées."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des actualités récupérée avec succès"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            )
    })
    public ResponseEntity<List<ActualiteResponseDto>> obtenirToutesLesActualites() {

        List<ActualiteResponseDto> actualites =
                actualiteService.obtenirToutesLesActualites();

        return ResponseEntity.ok(actualites);
    }


    // =========================================================
    // OBTENIR UNE ACTUALITE PAR ID
    // =========================================================

    @GetMapping("/{idActualite}")
    @Operation(
            summary = "Consulter une actualité",
            description = "Récupère une actualité à partir de son identifiant unique."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Actualité trouvée"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Actualité introuvable"
            )
    })
    public ResponseEntity<ActualiteResponseDto> obtenirActualiteParId(

            @Parameter(
                    description = "Identifiant unique de l'actualité",
                    required = true,
                    example = "1"
            )
            @PathVariable Long idActualite
    ) {

        ActualiteResponseDto actualite =
                actualiteService.obtenirActualiteParId(idActualite);

        return ResponseEntity.ok(actualite);
    }


    // =========================================================
    // MODIFIER UNE ACTUALITE
    // =========================================================

    @PutMapping("/{idActualite}")
    @Operation(
            summary = "Modifier une actualité",
            description = "Permet à un administrateur de modifier une actualité existante."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Actualité modifiée avec succès"
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
                    description = "Actualité introuvable"
            )
    })
    public ResponseEntity<ActualiteResponseDto> modifierActualite(

            @Parameter(
                    description = "Identifiant unique de l'actualité à modifier",
                    required = true,
                    example = "1"
            )
            @PathVariable Long idActualite,

            @Valid @RequestBody ActualiteRequestDto dto
    ) {

        ActualiteResponseDto actualite =
                actualiteService.modifierActualite(
                        idActualite,
                        dto
                );

        return ResponseEntity.ok(actualite);
    }


    // =========================================================
    // SUPPRIMER UNE ACTUALITE
    // =========================================================

    @DeleteMapping("/{idActualite}")
    @Operation(
            summary = "Supprimer une actualité",
            description = "Permet à un administrateur de supprimer une actualité existante."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Actualité supprimée avec succès"
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
                    description = "Actualité introuvable"
            )
    })
    public ResponseEntity<Void> supprimerActualite(

            @Parameter(
                    description = "Identifiant unique de l'actualité à supprimer",
                    required = true,
                    example = "1"
            )
            @PathVariable Long idActualite
    ) {

        actualiteService.supprimerActualite(idActualite);

        return ResponseEntity.noContent().build();
    }
}
