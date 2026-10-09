package com.alert360.controller;

import com.alert360.controller.dto.AgentStructureRequestDto;
import com.alert360.controller.dto.AgentStructureResponseDto;
import com.alert360.controller.dto.AgentStructureUpdateRequestDto;
import com.alert360.service.serviceInter.AgentStructureService;
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
@RequestMapping("/api/agents")
@RequiredArgsConstructor
@Tag(
        name = "Agents de structure",
        description = "Gestion des agents rattachés aux structures compétentes"
)
@SecurityRequirement(name = "bearerAuth")
public class AgentStructureController {

    private final AgentStructureService agentStructureService;


    // =========================================================
    // CREER UN AGENT
    // =========================================================

    @PostMapping("/structure/{idStructure}")
    @Operation(
            summary = "Créer un agent de structure",
            description = "Permet de créer un nouvel agent et de le rattacher à une structure compétente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Agent créé avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de l'agent invalides"
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
                    description = "Structure introuvable"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Un agent avec ces informations existe déjà"
            )
    })
    public ResponseEntity<AgentStructureResponseDto> creerAgent(

            @Parameter(
                    description = "Identifiant unique de la structure à laquelle rattacher l'agent",
                    required = true,
                    example = "1"
            )
            @PathVariable Long idStructure,

            @Valid @RequestBody AgentStructureRequestDto dto
    ) {

        AgentStructureResponseDto nouveauAgent =
                agentStructureService.creerAgent(dto);

        return new ResponseEntity<>(
                nouveauAgent,
                HttpStatus.CREATED
        );
    }


    // =========================================================
    // MODIFIER UN AGENT
    // =========================================================

    @PutMapping("/{idUtilisateur}")
    @Operation(
            summary = "Modifier un agent de structure",
            description = "Permet de modifier les informations d'un agent de structure existant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agent modifié avec succès"
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
                    description = "Agent introuvable"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Les informations fournies sont déjà utilisées"
            )
    })
    public ResponseEntity<AgentStructureResponseDto> modifierAgent(

            @Parameter(
                    description = "Identifiant de l'utilisateur correspondant à l'agent",
                    required = true,
                    example = "5"
            )
            @PathVariable Long idUtilisateur,
            @Valid @RequestBody AgentStructureRequestDto dto) {
        AgentStructureResponseDto agentModifie = agentStructureService.modifierAgent(idUtilisateur, dto);
        return ResponseEntity.ok(agentModifie);
    }


    // =========================================================
    // OBTENIR UN AGENT PAR ID
    // =========================================================

    @GetMapping("/{idUtilisateur}")
    @Operation(
            summary = "Consulter un agent",
            description = "Récupère les informations d'un agent de structure à partir de son identifiant utilisateur."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agent trouvé avec succès"
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
                    description = "Agent introuvable"
            )
    })
    public ResponseEntity<AgentStructureResponseDto> obtenirParId(

            @Parameter(
                    description = "Identifiant utilisateur de l'agent",
                    required = true,
                    example = "5"
            )
            @PathVariable Long idUtilisateur
    ) {

        AgentStructureResponseDto agent =
                agentStructureService.obtenirParId(idUtilisateur);

        return ResponseEntity.ok(agent);
    }


    // =========================================================
    // OBTENIR TOUS LES AGENTS
    // =========================================================

    @GetMapping
    @Operation(
            summary = "Lister tous les agents",
            description = "Récupère la liste de tous les agents de structure enregistrés."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des agents récupérée avec succès"
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
    public ResponseEntity<List<AgentStructureResponseDto>> obtenirTousLesAgents() {

        List<AgentStructureResponseDto> agents =
                agentStructureService.obtenirTousLesAgents();

        return ResponseEntity.ok(agents);
    }


    // =========================================================
    // OBTENIR LES AGENTS D'UNE STRUCTURE
    // =========================================================

    @GetMapping("/structure/{idStructure}")
    @Operation(
            summary = "Lister les agents d'une structure",
            description = "Récupère tous les agents rattachés à une structure compétente donnée."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des agents de la structure récupérée avec succès"
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
                    description = "Structure introuvable"
            )
    })
    public ResponseEntity<List<AgentStructureResponseDto>> obtenirAgentsParStructure(

            @Parameter(
                    description = "Identifiant unique de la structure",
                    required = true,
                    example = "1"
            )
            @PathVariable Long idStructure
    ) {

        List<AgentStructureResponseDto> agents =
                agentStructureService.obtenirAgentsParStructure(idStructure);

        return ResponseEntity.ok(agents);
    }


    // =========================================================
    // OBTENIR UN AGENT PAR MATRICULE
    // =========================================================

    @GetMapping("/matricule/{matricule}")
    @Operation(
            summary = "Rechercher un agent par matricule",
            description = "Récupère les informations d'un agent à partir de son matricule professionnel."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agent trouvé avec succès"
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
                    description = "Aucun agent trouvé avec ce matricule"
            )
    })
    public ResponseEntity<AgentStructureResponseDto> obtenirParMatricule(

            @Parameter(
                    description = "Matricule professionnel de l'agent",
                    required = true,
                    example = "AGT-001"
            )
            @PathVariable String matricule
    ) {

        AgentStructureResponseDto agent =
                agentStructureService.obtenirParMatricule(matricule);

        return ResponseEntity.ok(agent);
    }


    // =========================================================
    // SUPPRIMER UN AGENT
    // =========================================================

    @DeleteMapping("/{idUtilisateur}")
    @Operation(
            summary = "Supprimer un agent",
            description = "Supprime le compte d'un agent de structure à partir de son identifiant utilisateur."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Agent supprimé avec succès"
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
                    description = "Agent introuvable"
            )
    })
    public ResponseEntity<Void> supprimerAgent(

            @Parameter(
                    description = "Identifiant utilisateur de l'agent à supprimer",
                    required = true,
                    example = "5"
            )
            @PathVariable Long idUtilisateur
    ) {

        agentStructureService.supprimerAgent(idUtilisateur);

        return ResponseEntity.noContent().build();
    }
}
