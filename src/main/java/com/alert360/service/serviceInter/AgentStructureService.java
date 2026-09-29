package com.alert360.service.serviceInter;

import com.alert360.controller.dto.AgentStructureRequestDto;
import com.alert360.controller.dto.AgentStructureResponseDto;

import java.util.List;

public interface AgentStructureService {

    AgentStructureResponseDto creerAgent(AgentStructureRequestDto dto, Long idStructure);

    AgentStructureResponseDto modifierAgent(Long idUtilisateur, AgentStructureRequestDto dto);

    AgentStructureResponseDto obtenirParId(Long idUtilisateur);

    List<AgentStructureResponseDto> obtenirTousLesAgents();

    List<AgentStructureResponseDto> obtenirAgentsParStructure(Long idStructure);

    AgentStructureResponseDto obtenirParMatricule(String matricule);

    void supprimerAgent(Long idUtilisateur);
}
