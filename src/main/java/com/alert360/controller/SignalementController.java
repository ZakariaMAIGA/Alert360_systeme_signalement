package com.alert360.controller;

import com.alert360.controller.dto.AssignationSignalementRequestDto;
import com.alert360.controller.dto.SignalementRequestDto;
import com.alert360.controller.dto.SignalementResponseDto;
import com.alert360.entity.enums.EnumStatut;
import com.alert360.service.serviceInter.SignalementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@RequestMapping("/api/signalements")
@RequiredArgsConstructor
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.PATCH,
                RequestMethod.DELETE
        }
)
@Tag(
        name = "Signalements",
        description = "Gestion des signalements citoyens, de leur traitement, de leur statut et de leur assignation"
)
@SecurityRequirement(name = "bearerAuth")
public class SignalementController {


    private final SignalementService signalementService;


    // ==========================================================
    // CREER UN SIGNALEMENT
    // ==========================================================

    @PostMapping
    @Operation(
            summary = "Créer un signalement",
            description = "Permet de créer un nouveau signalement citoyen. " +
                    "La structure compétente peut être déterminée automatiquement à partir de la localisation GPS du signalement."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Signalement créé avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données du signalement invalides"
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
                    description = "Catégorie, citoyen ou structure introuvable"
            )
    })
    public ResponseEntity<SignalementResponseDto> creerSignalement(
            @Valid @RequestBody SignalementRequestDto dto
    ) {

        SignalementResponseDto signalement =
                signalementService.creerSignalement(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(signalement);
    }


    // ==========================================================
    // OBTENIR TOUS LES SIGNALEMENTS
    // ==========================================================

    @GetMapping
    @Operation(
            summary = "Obtenir tous les signalements",
            description = "Retourne la liste de tous les signalements enregistrés dans le système."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des signalements récupérée avec succès"
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
    public ResponseEntity<List<SignalementResponseDto>> obtenirTousLesSignalements() {

        return ResponseEntity.ok(
                signalementService.obtenirTousLesSignalements()
        );
    }


    // ==========================================================
    // OBTENIR UN SIGNALEMENT PAR SON ID
    // ==========================================================

    @GetMapping("/{idSignalement}")
    @Operation(
            summary = "Obtenir un signalement par son ID",
            description = "Retourne les informations détaillées d'un signalement à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Signalement trouvé avec succès"
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
            )
    })
    public ResponseEntity<SignalementResponseDto> obtenirParId(

            @Parameter(
                    description = "Identifiant unique du signalement",
                    example = "1",
                    required = true
            )
            @PathVariable Long idSignalement
    ) {

        return ResponseEntity.ok(
                signalementService.obtenirParId(idSignalement)
        );
    }


    // ==========================================================
    // MODIFIER UN SIGNALEMENT
    // ==========================================================

    @PutMapping("/{idSignalement}")
    @Operation(
            summary = "Modifier un signalement",
            description = "Permet de modifier un signalement existant. " +
                    "Si la localisation GPS est modifiée, la structure compétente peut être recalculée."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Signalement modifié avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données du signalement invalides"
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
                    description = "Signalement, catégorie ou structure introuvable"
            )
    })
    public ResponseEntity<SignalementResponseDto> modifierSignalement(

            @Parameter(
                    description = "Identifiant unique du signalement à modifier",
                    example = "1",
                    required = true
            )
            @PathVariable Long idSignalement,

            @Valid @RequestBody SignalementRequestDto dto
    ) {

        return ResponseEntity.ok(
                signalementService.modifierSignalement(
                        idSignalement,
                        dto
                )
        );
    }


    // ==========================================================
    // CHANGER LE STATUT
    // ==========================================================

    @PatchMapping("/{idSignalement}/statut")
    @Operation(
            summary = "Changer le statut d'un signalement",
            description = "Permet de modifier le statut d'un signalement : DECLARE, EN_COURS, RESOLU ou REJETE."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Statut du signalement modifié avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Statut invalide"
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
            )
    })
    public ResponseEntity<SignalementResponseDto> changerStatut(

            @Parameter(
                    description = "Identifiant unique du signalement",
                    example = "1",
                    required = true
            )
            @PathVariable Long idSignalement,

            @Parameter(
                    description = "Nouveau statut du signalement",
                    example = "EN_COURS",
                    required = true,
                    schema = @Schema(
                            implementation = EnumStatut.class
                    )
            )
            @RequestParam EnumStatut statut
    ) {

        return ResponseEntity.ok(
                signalementService.changerStatut(
                        idSignalement,
                        statut
                )
        );
    }


    // ==========================================================
    // ASSIGNER / REASSIGNER UNE STRUCTURE
    // ==========================================================

    @PatchMapping("/{idSignalement}/assigner")
    @Operation(
            summary = "Assigner un signalement à une structure",
            description = "Permet d'assigner ou de réassigner manuellement un signalement à une structure compétente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Signalement assigné à la structure avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Paramètre invalide"
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
                    description = "Signalement ou structure introuvable"
            )
    })
    public ResponseEntity<SignalementResponseDto> assignerStructure(

            @Parameter(
                    description = "Identifiant unique du signalement",
                    example = "1",
                    required = true
            )
            @PathVariable Long idSignalement,

            @Parameter(
                    description = "Identifiant de la structure compétente",
                    example = "2",
                    required = true
            )
            @RequestParam Long idStructure
    ) {

        return ResponseEntity.ok(
                signalementService.assignerStructure(
                        idSignalement,
                        idStructure
                )
        );
    }


    // ==========================================================
    // ASSIGNER UN AGENT DE TERRAIN
    // ==========================================================

    @PatchMapping("/{idSignalement}/assigner-agent")
    @Operation(
            summary = "Assigner un agent de terrain",
            description = "Permet d'assigner un agent de terrain à un signalement. " +
                    "Cette opération est réservée au responsable de la structure."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agent assigné au signalement avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données d'assignation invalides"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès interdit ou utilisateur non autorisé"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Signalement ou agent introuvable"
            )
    })
    public ResponseEntity<SignalementResponseDto> assignerAgent(

            @Parameter(
                    description = "Identifiant unique du signalement",
                    example = "1",
                    required = true
            )
            @PathVariable Long idSignalement,

            @Valid @RequestBody AssignationSignalementRequestDto dto
    ) {

        return ResponseEntity.ok(
                signalementService.assignerAgent(
                        idSignalement,
                        dto
                )
        );
    }


    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UN CITOYEN
    // ==========================================================

    @GetMapping("/citoyen/{idCitoyen}")
    @Operation(
            summary = "Obtenir les signalements d'un citoyen",
            description = "Retourne tous les signalements créés par un citoyen donné."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Signalements du citoyen récupérés avec succès"
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
    public ResponseEntity<List<SignalementResponseDto>> obtenirParCitoyen(

            @Parameter(
                    description = "Identifiant unique du citoyen",
                    example = "1",
                    required = true
            )
            @PathVariable Long idCitoyen
    ) {

        return ResponseEntity.ok(
                signalementService.obtenirParCitoyen(idCitoyen)
        );
    }


    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UNE STRUCTURE
    // ==========================================================

    @GetMapping("/structure/{idStructure}")
    @Operation(
            summary = "Obtenir les signalements d'une structure",
            description = "Retourne tous les signalements assignés à une structure compétente donnée."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Signalements de la structure récupérés avec succès"
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
    public ResponseEntity<List<SignalementResponseDto>> obtenirParStructure(

            @Parameter(
                    description = "Identifiant unique de la structure",
                    example = "1",
                    required = true
            )
            @PathVariable Long idStructure
    ) {

        return ResponseEntity.ok(
                signalementService.obtenirParStructure(idStructure)
        );
    }


    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UN AGENT
    // ==========================================================

    @GetMapping("/agent/{idAgent}")
    @Operation(
            summary = "Obtenir les signalements assignés à un agent",
            description = "Retourne tous les signalements assignés à un agent de terrain donné."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Signalements de l'agent récupérés avec succès"
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
                    description = "Agent introuvable"
            )
    })
    public ResponseEntity<List<SignalementResponseDto>> obtenirParAgentAssigne(

            @Parameter(
                    description = "Identifiant unique de l'agent",
                    example = "1",
                    required = true
            )
            @PathVariable Long idAgent
    ) {

        return ResponseEntity.ok(
                signalementService.obtenirParAgentAssigne(idAgent)
        );
    }


    // ==========================================================
    // OBTENIR LES SIGNALEMENTS PAR STATUT
    // ==========================================================

    @GetMapping("/statut/{statut}")
    @Operation(
            summary = "Obtenir les signalements par statut",
            description = "Retourne tous les signalements correspondant à un statut donné."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Signalements récupérés avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Statut invalide"
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
    public ResponseEntity<List<SignalementResponseDto>> obtenirParStatut(

            @Parameter(
                    description = "Statut recherché",
                    example = "RESOLU",
                    required = true,
                    schema = @Schema(
                            implementation = EnumStatut.class
                    )
            )
            @PathVariable EnumStatut statut
    ) {

        return ResponseEntity.ok(
                signalementService.obtenirParStatut(statut)
        );
    }


    // ==========================================================
    // SUPPRIMER UN SIGNALEMENT
    // ==========================================================

    @DeleteMapping("/{idSignalement}")
    @Operation(
            summary = "Supprimer un signalement",
            description = "Supprime un signalement existant à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Signalement supprimé avec succès"
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
            )
    })
    public ResponseEntity<Void> supprimerSignalement(

            @Parameter(
                    description = "Identifiant unique du signalement à supprimer",
                    example = "1",
                    required = true
            )
            @PathVariable Long idSignalement
    ) {

        signalementService.supprimerSignalement(idSignalement);

        return ResponseEntity.noContent().build();
    }
}