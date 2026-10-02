package com.alert360.mapper.Response;

import com.alert360.controller.dto.AgentStructureResponseDto;
import com.alert360.entity.AgentStructure;
import org.springframework.stereotype.Component;

@Component
public class AgentStructureResponseMapper {

    public AgentStructureResponseDto toDto(AgentStructure entity) {
        if (entity == null) {
            return null;
        }

        // Utilisation du Builder du DTO pour une instanciation propre et lisible
        return AgentStructureResponseDto.builder()
                .idUtilisateur(entity.getIdUtilisateur())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .telephone(entity.getTelephone())
                .email(entity.getEmail())
                .matriculeAgent(entity.getMatriculeAgent())
                .estResponsable(entity.getEstResponsable()) // Corrigé : utilise getEstResponsable() (type Boolean) au lieu de isEstResponsable()
                .role(entity.getRole())
                .estActif(entity.getEstActif())
                .dateCreation(entity.getDateCreation())
                .idStructure(entity.getStructure() != null ? entity.getStructure().getIdStructure() : null)
                .nomStructure(entity.getStructure() != null ? entity.getStructure().getNomStructure() : null)
                .build();
    }
}