package com.alert360.controller;

import com.alert360.controller.dto.PreuveResolutionRequestDto;
import com.alert360.controller.dto.PreuveResolutionResponseDto;
import com.alert360.service.serviceInter.PreuveResolutionService;
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
@RequestMapping("/api/preuves")
@RequiredArgsConstructor
@Tag(
        name = "Preuves de résolution",
        description = "Gestion des preuves permettant de justifier la résolution des signalements"
)
@SecurityRequirement(name = "bearerAuth")
public class PreuveResolutionController {

    private final PreuveResolutionService preuveResolutionService;


    // ============================================================
    // AJOUTER UNE PREUVE DE RESOLUTION
    // ============================================================

    @PostMapping
    @Operation(
            summary = "Ajouter une preuve de résolution",
            description = "Permet d'ajouter une preuve permettant de justifier la résolution d'un signalement."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Preuve de résolution ajoutée avec succès"
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
                    description = "Signalement introuvable"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Une preuve de résolution existe déjà pour ce signalement"
            )
    })
    public ResponseEntity<PreuveResolutionResponseDto> ajouterPreuveResolution(
            @Valid @RequestBody PreuveResolutionRequestDto dto
    ) {

        PreuveResolutionResponseDto preuve =
                preuveResolutionService.ajouterPreuveResolution(dto);

        return new ResponseEntity<>(
                preuve,
                HttpStatus.CREATED
        );
    }


    // ============================================================
    // MODIFIER UNE PREUVE DE RESOLUTION
    // ============================================================

    @PutMapping("/{idPreuve}")
    @Operation(
            summary = "Modifier une preuve de résolution",
            description = "Permet de modifier une preuve de résolution existante."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Preuve de résolution modifiée avec succès"
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
                    description = "Preuve de résolution introuvable"
            )
    })
    public ResponseEntity<PreuveResolutionResponseDto> modifierPreuveResolution(

            @Parameter(
                    description = "Identifiant unique de la preuve de résolution",
                    example = "1",
                    required = true
            )
            @PathVariable Long idPreuve,

            @Valid @RequestBody PreuveResolutionRequestDto dto
    ) {

        PreuveResolutionResponseDto preuveModifiee =
                preuveResolutionService.modifierPreuveResolution(
                        idPreuve,
                        dto
                );

        return ResponseEntity.ok(preuveModifiee);
    }


    // ============================================================
    // OBTENIR UNE PREUVE PAR ID
    // ============================================================

    @GetMapping("/{idPreuve}")
    @Operation(
            summary = "Obtenir une preuve de résolution par son ID",
            description = "Retourne les informations d'une preuve de résolution à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Preuve de résolution trouvée avec succès"
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
                    description = "Preuve de résolution introuvable"
            )
    })
    public ResponseEntity<PreuveResolutionResponseDto> obtenirParId(

            @Parameter(
                    description = "Identifiant unique de la preuve de résolution",
                    example = "1",
                    required = true
            )
            @PathVariable Long idPreuve
    ) {

        PreuveResolutionResponseDto preuve =
                preuveResolutionService.obtenirParId(idPreuve);

        return ResponseEntity.ok(preuve);
    }


    // ============================================================
    // OBTENIR LA PREUVE D'UN SIGNALEMENT
    // ============================================================

    @GetMapping("/signalement/{idSignalement}")
    @Operation(
            summary = "Obtenir la preuve d'un signalement",
            description = "Retourne la preuve de résolution associée à un signalement donné."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Preuve de résolution trouvée avec succès"
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
                    description = "Aucune preuve de résolution trouvée pour ce signalement"
            )
    })
    public ResponseEntity<PreuveResolutionResponseDto> obtenirParSignalement(

            @Parameter(
                    description = "Identifiant unique du signalement",
                    example = "1",
                    required = true
            )
            @PathVariable Long idSignalement
    ) {

        PreuveResolutionResponseDto preuve =
                preuveResolutionService.obtenirParSignalement(idSignalement);

        return ResponseEntity.ok(preuve);
    }


    // ============================================================
    // OBTENIR TOUTES LES PREUVES
    // ============================================================

    @GetMapping
    @Operation(
            summary = "Obtenir toutes les preuves de résolution",
            description = "Retourne la liste de toutes les preuves de résolution enregistrées."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des preuves récupérée avec succès"
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
    public ResponseEntity<List<PreuveResolutionResponseDto>> obtenirToutesLesPreuves() {

        List<PreuveResolutionResponseDto> preuves =
                preuveResolutionService.obtenirToutesLesPreuves();

        return ResponseEntity.ok(preuves);
    }


    // ============================================================
    // SUPPRIMER UNE PREUVE
    // ============================================================

    @DeleteMapping("/{idPreuve}")
    @Operation(
            summary = "Supprimer une preuve de résolution",
            description = "Supprime une preuve de résolution existante à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Preuve de résolution supprimée avec succès"
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
                    description = "Preuve de résolution introuvable"
            )
    })
    public ResponseEntity<Void> supprimerPreuve(

            @Parameter(
                    description = "Identifiant unique de la preuve à supprimer",
                    example = "1",
                    required = true
            )
            @PathVariable Long idPreuve
    ) {

        preuveResolutionService.supprimerPreuve(idPreuve);

        return ResponseEntity.noContent().build();
    }
}