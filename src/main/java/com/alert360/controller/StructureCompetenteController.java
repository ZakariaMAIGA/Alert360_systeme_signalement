package com.alert360.controller;

import com.alert360.controller.dto.StructureCompetenteRequestDto;
import com.alert360.controller.dto.StructureCompetenteResponseDto;
import com.alert360.entity.enums.EnumTypeStructure;
import com.alert360.service.serviceInter.StructureCompetenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/api/structures")
@RequiredArgsConstructor
@Tag(
        name = "Structures compétentes",
        description = "Gestion des structures compétentes chargées de traiter les signalements citoyens"
)
@SecurityRequirement(name = "bearerAuth")
public class StructureCompetenteController {

    private final StructureCompetenteService structureCompetenteService;

    // =========================
    // CREER UNE STRUCTURE
    // =========================
    @PostMapping
    @Operation(
            summary = "Créer une structure compétente",
            description = "Permet à un administrateur de créer une nouvelle structure compétente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Structure créée avec succès"
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
                    description = "Une structure avec les mêmes informations existe déjà"
            )
    })
    public ResponseEntity<StructureCompetenteResponseDto> creerStructure(
            @Valid @RequestBody StructureCompetenteRequestDto dto
    ) {
        StructureCompetenteResponseDto response =
                structureCompetenteService.creerStructure(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================
    // OBTENIR TOUTES LES STRUCTURES
    // =========================
    @GetMapping
    @Operation(
            summary = "Obtenir toutes les structures",
            description = "Retourne la liste de toutes les structures compétentes enregistrées."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des structures récupérée avec succès"
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
    public ResponseEntity<List<StructureCompetenteResponseDto>> obtenirToutesLesStructures() {

        List<StructureCompetenteResponseDto> structures =
                structureCompetenteService.obtenirToutesLesStructures();

        return ResponseEntity.ok(structures);
    }

    // =========================
    // OBTENIR UNE STRUCTURE PAR ID
    // =========================
    @GetMapping("/{idStructure}")
    @Operation(
            summary = "Obtenir une structure par son identifiant",
            description = "Retourne les informations détaillées d'une structure compétente à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Structure trouvée avec succès"
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
                    description = "Structure introuvable"
            )
    })
    public ResponseEntity<StructureCompetenteResponseDto> obtenirStructureParId(

            @Parameter(
                    description = "Identifiant unique de la structure",
                    example = "1",
                    required = true
            )
            @PathVariable Long idStructure
    ) {
        StructureCompetenteResponseDto structure =
                structureCompetenteService.obtenirParId(idStructure);

        return ResponseEntity.ok(structure);
    }

    // =========================
    // OBTENIR LES STRUCTURES PAR TYPE
    // =========================
    @GetMapping("/type/{typeStructure}")
    @Operation(
            summary = "Obtenir les structures par type",
            description = "Retourne toutes les structures compétentes correspondant au type demandé."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Structures récupérées avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Type de structure invalide"
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
    public ResponseEntity<List<StructureCompetenteResponseDto>> obtenirParType(

            @Parameter(
                    description = "Type de la structure recherchée",
                    required = true,
                    schema = @Schema(implementation = EnumTypeStructure.class),
                    example = "MAIRIE"
            )
            @PathVariable EnumTypeStructure typeStructure
    ) {

        // Correction de la méthode du service (obtenirPartype -> obtenirParType)
        List<StructureCompetenteResponseDto> structures =
                structureCompetenteService.obtenirParType(typeStructure);

        return ResponseEntity.ok(structures);
    }

    // =========================
    // MODIFIER UNE STRUCTURE
    // =========================
    @PutMapping("/{idStructure}")
    @Operation(
            summary = "Modifier une structure",
            description = "Permet de modifier les informations d'une structure compétente existante."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Structure modifiée avec succès"
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
                    description = "Structure introuvable"
            )
    })
    public ResponseEntity<StructureCompetenteResponseDto> modifierStructure(

            @Parameter(
                    description = "Identifiant unique de la structure à modifier",
                    example = "1",
                    required = true
            )
            @PathVariable Long idStructure,

            @Valid @RequestBody StructureCompetenteRequestDto dto
    ) {
        StructureCompetenteResponseDto structure =
                structureCompetenteService.modifierStructure(
                        idStructure,
                        dto
                );

        return ResponseEntity.ok(structure);
    }

    // =========================
    // SUPPRIMER UNE STRUCTURE
    // =========================
    @DeleteMapping("/{idStructure}")
    @Operation(
            summary = "Supprimer une structure",
            description = "Supprime une structure compétente à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Structure supprimée avec succès"
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
                    description = "Structure introuvable"
            )
    })
    public ResponseEntity<Void> supprimerStructure(

            @Parameter(
                    description = "Identifiant unique de la structure à supprimer",
                    example = "1",
                    required = true
            )
            @PathVariable Long idStructure
    ) {
        // Correction de la méthode du service (supprimerStructre -> supprimerStructure)
        structureCompetenteService.supprimerStructure(idStructure);

        return ResponseEntity.noContent().build();
    }
}