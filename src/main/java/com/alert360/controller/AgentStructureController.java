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
        description = "Gestion des agents rattachés aux structures compétentes de la plateforme Alert'360"
)
@SecurityRequirement(name = "bearerAuth")
public class AgentStructureController {

    private final AgentStructureService agentStructureService;

    // =========================
    // CREER UN AGENT
    // =========================
    @PostMapping("/structure/{idStructure}")
    @Operation(
            summary = "Créer un agent pour une structure",
            description = "Crée un nouvel agent de structure à partir des informations fournies."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agent créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "404", description = "Structure introuvable"),
            @ApiResponse(responseCode = "409", description = "Matricule ou informations uniques déjà utilisés")
    })
    public ResponseEntity<AgentStructureResponseDto> creerAgent(

            @Parameter(
                    description = "Identifiant de la structure à laquelle rattacher l'agent",
                    example = "1",
                    required = true
            )
            @PathVariable Long idStructure,

            @Valid @RequestBody AgentStructureRequestDto dto
    ) {
        AgentStructureResponseDto nouveauAgent =
                agentStructureService.creerAgent(dto);

        return new ResponseEntity<>(nouveauAgent, HttpStatus.CREATED);
    }

    // =========================
    // MODIFIER UN AGENT
    // =========================
    @PutMapping("/{idUtilisateur}")
    @Operation(
            summary = "Modifier un agent",
            description = "Met à jour les informations d'un agent de structure existant."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agent modifié avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "404", description = "Agent introuvable")
    })
    public ResponseEntity<AgentStructureResponseDto> modifierAgent(

            @Parameter(
                    description = "Identifiant de l'utilisateur correspondant à l'agent",
                    example = "1",
                    required = true
            )
            @PathVariable Long idUtilisateur,

            @Valid @RequestBody AgentStructureUpdateRequestDto dto
    ) {
        AgentStructureResponseDto agentModifie =
                agentStructureService.modifierAgent(idUtilisateur, dto);

        return ResponseEntity.ok(agentModifie);
    }

    // =========================
    // OBTENIR UN AGENT PAR ID
    // =========================
    @GetMapping("/{idUtilisateur}")
    @Operation(
            summary = "Obtenir un agent par son identifiant",
            description = "Retourne les informations d'un agent de structure à partir de son identifiant utilisateur."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agent trouvé avec succès"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "404", description = "Agent introuvable")
    })
    public ResponseEntity<AgentStructureResponseDto> obtenirParId(

            @Parameter(
                    description = "Identifiant de l'utilisateur correspondant à l'agent",
                    example = "1",
                    required = true
            )
            @PathVariable Long idUtilisateur
    ) {
        AgentStructureResponseDto agent =
                agentStructureService.obtenirParId(idUtilisateur);

        return ResponseEntity.ok(agent);
    }

    // =========================
    // OBTENIR TOUS LES AGENTS
    // =========================
    @GetMapping
    @Operation(
            summary = "Obtenir tous les agents",
            description = "Retourne la liste de tous les agents de structure enregistrés sur la plateforme."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des agents récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    public ResponseEntity<List<AgentStructureResponseDto>> obtenirTousLesAgents() {

        List<AgentStructureResponseDto> agents =
                agentStructureService.obtenirTousLesAgents();

        return ResponseEntity.ok(agents);
    }

    // =========================
    // OBTENIR LES AGENTS D'UNE STRUCTURE
    // =========================
    @GetMapping("/structure/{idStructure}")
    @Operation(
            summary = "Obtenir les agents d'une structure",
            description = "Retourne tous les agents rattachés à une structure compétente donnée."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agents de la structure récupérés avec succès"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "404", description = "Structure introuvable")
    })
    public ResponseEntity<List<AgentStructureResponseDto>> obtenirAgentsParStructure(

            @Parameter(
                    description = "Identifiant de la structure compétente",
                    example = "1",
                    required = true
            )
            @PathVariable Long idStructure
    ) {
        List<AgentStructureResponseDto> agents =
                agentStructureService.obtenirAgentsParStructure(idStructure);

        return ResponseEntity.ok(agents);
    }

    // =========================
    // OBTENIR UN AGENT PAR MATRICULE
    // =========================
    @GetMapping("/matricule/{matricule}")
    @Operation(
            summary = "Rechercher un agent par matricule",
            description = "Retourne les informations de l'agent correspondant au matricule fourni."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agent trouvé avec succès"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "404", description = "Aucun agent trouvé avec ce matricule")
    })
    public ResponseEntity<AgentStructureResponseDto> obtenirParMatricule(

            @Parameter(
                    description = "Matricule unique de l'agent",
                    example = "AGT-001",
                    required = true
            )
            @PathVariable String matricule
    ) {
        AgentStructureResponseDto agent =
                agentStructureService.obtenirParMatricule(matricule);

        return ResponseEntity.ok(agent);
    }

    // =========================
    // SUPPRIMER UN AGENT
    // =========================
    @DeleteMapping("/{idUtilisateur}")
    @Operation(
            summary = "Supprimer un agent",
            description = "Supprime un agent de structure à partir de son identifiant utilisateur."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Agent supprimé avec succès"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "404", description = "Agent introuvable")
    })
    public ResponseEntity<Void> supprimerAgent(

            @Parameter(
                    description = "Identifiant de l'utilisateur correspondant à l'agent à supprimer",
                    example = "1",
                    required = true
            )
            @PathVariable Long idUtilisateur
    ) {
        agentStructureService.supprimerAgent(idUtilisateur);

        return ResponseEntity.noContent().build();
    }
}