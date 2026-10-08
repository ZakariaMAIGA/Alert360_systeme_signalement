package com.alert360.controller;

import com.alert360.controller.dto.ContenuEducatifRequestDto;
import com.alert360.controller.dto.ContenuEducatifResponseDto;
import com.alert360.service.serviceInter.ContenuEducatifService;
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
@RequestMapping("/api/contenus")
@RequiredArgsConstructor
@Tag(
        name = "Contenus éducatifs",
        description = "Gestion des contenus éducatifs publiés sur la plateforme Alert'360"
)
@SecurityRequirement(name = "bearerAuth")
public class ContenuEducatifController {

    private final ContenuEducatifService contenuEducatifService;


    // ============================================================
    // CREER UN CONTENU EDUCATIF
    // ============================================================

    @PostMapping
    @Operation(
            summary = "Créer un contenu éducatif",
            description = "Permet de créer et publier un nouveau contenu éducatif."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Contenu éducatif créé avec succès"
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
                    description = "Auteur introuvable"
            )
    })
    public ResponseEntity<ContenuEducatifResponseDto> creerContenu(
            @Valid @RequestBody ContenuEducatifRequestDto dto
    ) {

        ContenuEducatifResponseDto response =
                contenuEducatifService.creerContenu(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // ============================================================
    // OBTENIR TOUS LES CONTENUS
    // ============================================================

    @GetMapping
    @Operation(
            summary = "Obtenir tous les contenus éducatifs",
            description = "Retourne la liste de tous les contenus éducatifs disponibles."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des contenus récupérée avec succès"
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
    public ResponseEntity<List<ContenuEducatifResponseDto>> obtenirTousLesContenus() {

        List<ContenuEducatifResponseDto> contenus =
                contenuEducatifService.obtenirTousLesContenus();

        return ResponseEntity.ok(contenus);
    }


    // ============================================================
    // OBTENIR UN CONTENU PAR ID
    // ============================================================

    @GetMapping("/{idContenu}")
    @Operation(
            summary = "Obtenir un contenu éducatif par son ID",
            description = "Retourne les informations détaillées d'un contenu éducatif à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Contenu éducatif trouvé avec succès"
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
                    description = "Contenu éducatif introuvable"
            )
    })
    public ResponseEntity<ContenuEducatifResponseDto> obtenirContenuParId(

            @Parameter(
                    description = "Identifiant unique du contenu éducatif",
                    example = "1",
                    required = true
            )
            @PathVariable Long idContenu
    ) {

        ContenuEducatifResponseDto contenu =
                contenuEducatifService.obtenirParId(idContenu);

        return ResponseEntity.ok(contenu);
    }


    // ============================================================
    // MODIFIER UN CONTENU
    // ============================================================

    @PutMapping("/{idContenu}")
    @Operation(
            summary = "Modifier un contenu éducatif",
            description = "Permet de modifier les informations d'un contenu éducatif existant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Contenu éducatif modifié avec succès"
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
                    description = "Contenu éducatif introuvable"
            )
    })
    public ResponseEntity<ContenuEducatifResponseDto> modifierContenu(

            @Parameter(
                    description = "Identifiant unique du contenu éducatif à modifier",
                    example = "1",
                    required = true
            )
            @PathVariable Long idContenu,

            @Valid @RequestBody ContenuEducatifRequestDto dto
    ) {

        ContenuEducatifResponseDto contenu =
                contenuEducatifService.modifierContenu(
                        idContenu,
                        dto
                );

        return ResponseEntity.ok(contenu);
    }


    // ============================================================
    // SUPPRIMER UN CONTENU
    // ============================================================

    @DeleteMapping("/{idContenu}")
    @Operation(
            summary = "Supprimer un contenu éducatif",
            description = "Supprime un contenu éducatif existant à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Contenu éducatif supprimé avec succès"
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
                    description = "Contenu éducatif introuvable"
            )
    })
    public ResponseEntity<Void> supprimerContenu(

            @Parameter(
                    description = "Identifiant unique du contenu éducatif à supprimer",
                    example = "1",
                    required = true
            )
            @PathVariable Long idContenu
    ) {

        contenuEducatifService.supprimerContenu(idContenu);

        return ResponseEntity.noContent().build();
    }
}