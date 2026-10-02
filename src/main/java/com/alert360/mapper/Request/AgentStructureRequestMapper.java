package com.alert360.mapper.Request;

import com.alert360.controller.dto.AgentStructureRequestDto;
import com.alert360.entity.AgentStructure;
import com.alert360.entity.StructureCompetente;
import com.alert360.entity.enums.EnumRole;
import org.springframework.stereotype.Component;

@Component
public class AgentStructureRequestMapper {

    public AgentStructure toEntity(AgentStructureRequestDto dto, StructureCompetente structure) {

        if (dto == null) {
            return null;
        }

        AgentStructure agent = new AgentStructure();

        agent.setNom(dto.getNom());
        agent.setPrenom(dto.getPrenom());
        agent.setTelephone(dto.getTelephone());
        agent.setEmail(dto.getEmail());
        agent.setMotDePasse(dto.getMotDePasse());
        agent.setMatriculeAgent(dto.getMatriculeAgent());

        // 1. Définition du statut Responsable (lit la valeur du DTO, false par défaut si non spécifié)
        agent.setEstResponsable(Boolean.TRUE.equals(dto.getEstResponsable()));

        // 2. Un agent créé par l'administration ou par un responsable est ACTIF par défaut
        agent.setEstActif(true);

        // 3. Attribution du rôle et de la structure associée
        agent.setRole(EnumRole.STRUCTURE);
        agent.setStructure(structure);

        return agent;
    }
}