package com.alert360.service.serviceInter;

import com.alert360.controller.dto.AgentStructureRequestDto;
import com.alert360.controller.dto.AgentStructureResponseDto;
import com.alert360.controller.dto.AgentStructureUpdateRequestDto;

import java.util.List;

public interface AgentStructureService {

    /**
     * Crée un agent de structure (Admin ou Responsable de structure)
     */
    AgentStructureResponseDto creerAgent(AgentStructureRequestDto dto);

    /**
     * Modifie les informations d'un agent existant
     */
    AgentStructureResponseDto modifierAgent(Long idUtilisateur, AgentStructureUpdateRequestDto dto);

    /**
     * Récupère un agent par son identifiant unique
     */
    AgentStructureResponseDto obtenirParId(Long idUtilisateur);

    /**
     * Récupère la liste globale de tous les agents
     */
    List<AgentStructureResponseDto> obtenirTousLesAgents();

    /**
     * Récupère tous les agents rattachés à une structure donnée
     */
    List<AgentStructureResponseDto> obtenirAgentsParStructure(Long idStructure);

    /**
     * Récupère les responsables d'une structure donnée (estResponsable = true)
     */
    List<AgentStructureResponseDto> obtenirResponsablesParStructure(Long idStructure);

    /**
     * Récupère un agent par son matricule unique
     */
    AgentStructureResponseDto obtenirParMatricule(String matricule);

    /**
     * Supprime ou désactive un agent
     */
    void supprimerAgent(Long idUtilisateur);
}