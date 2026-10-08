package com.alert360.controller;

import com.alert360.controller.dto.ActionCitoyenneRequestDto;
import com.alert360.controller.dto.ActionCitoyenneResponseDto;
import com.alert360.service.serviceInter.ActionCitoyenneService;
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
@RequestMapping("/api/actions")
@RequiredArgsConstructor
@Tag(
        name = "Actions citoyennes",
        description = "Gestion des actions citoyennes organisées par les utilisateurs"
)
@SecurityRequirement(name = "bearerAuth")
public class ActionCitoyenneController {

    private final ActionCitoyenneService actionCitoyenneService;


    /**
     * Créer une action citoyenne
     */
    @PostMapping
    @Operation(
            summary = "Créer une action citoyenne",
            description = "Permet de créer une nouvelle action citoyenne."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Action citoyenne créée avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de l'action invalides"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé"
            )
    })
    public ResponseEntity<ActionCitoyenneResponseDto> creerAction(
            @Valid @RequestBody ActionCitoyenneRequestDto dto
    ) {

        ActionCitoyenneResponseDto nouvelleAction =
                actionCitoyenneService.creerAction(dto);

        return new ResponseEntity<>(nouvelleAction, HttpStatus.CREATED);
    }


    /**
     * Modifier une action citoyenne
     */
    @PutMapping("/{idAction}")
    @Operation(
            summary = "Modifier une action citoyenne",
            description = "Permet de modifier les informations d'une action citoyenne existante."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Action citoyenne modifiée avec succès"
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
                    description = "Accès refusé"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Action citoyenne introuvable"
            )
    })
    public ResponseEntity<ActionCitoyenneResponseDto> modifierAction(

            @Parameter(
                    description = "Identifiant unique de l'action citoyenne",
                    required = true,
                    example = "1"
            )
            @PathVariable Long idAction,

            @Valid @RequestBody ActionCitoyenneRequestDto dto
    ) {

        ActionCitoyenneResponseDto actionModifiee =
                actionCitoyenneService.modifierAction(idAction, dto);

        return ResponseEntity.ok(actionModifiee);
    }


    /**
     * Obtenir une action citoyenne par son identifiant
     */
    @GetMapping("/{idAction}")
    @Operation(
            summary = "Consulter une action citoyenne",
            description = "Récupère les informations détaillées d'une action citoyenne à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Action citoyenne trouvée"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Action citoyenne introuvable"
            )
    })
    public ResponseEntity<ActionCitoyenneResponseDto> obtenirParId(

            @Parameter(
                    description = "Identifiant unique de l'action citoyenne",
                    required = true,
                    example = "1"
            )
            @PathVariable Long idAction
    ) {

        ActionCitoyenneResponseDto action =
                actionCitoyenneService.obtenirParId(idAction);

        return ResponseEntity.ok(action);
    }


    /**
     * Obtenir toutes les actions citoyennes
     */
    @GetMapping
    @Operation(
            summary = "Lister les actions citoyennes",
            description = "Récupère la liste de toutes les actions citoyennes enregistrées."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des actions citoyennes récupérée avec succès"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            )
    })
    public ResponseEntity<List<ActionCitoyenneResponseDto>> obtenirToutesLesActions() {

        List<ActionCitoyenneResponseDto> actions =
                actionCitoyenneService.obtenirToutesLesActions();

        return ResponseEntity.ok(actions);
    }


    /**
     * Supprimer une action citoyenne
     */
    @DeleteMapping("/{idAction}")
    @Operation(
            summary = "Supprimer une action citoyenne",
            description = "Supprime une action citoyenne à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Action citoyenne supprimée avec succès"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Action citoyenne introuvable"
            )
    })
    public ResponseEntity<Void> supprimerAction(

            @Parameter(
                    description = "Identifiant unique de l'action citoyenne à supprimer",
                    required = true,
                    example = "1"
            )
            @PathVariable Long idAction
    ) {

        actionCitoyenneService.supprimerAction(idAction);

        return ResponseEntity.noContent().build();
    }
}
