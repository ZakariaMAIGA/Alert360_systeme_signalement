package com.alert360.mapper.Response;

import com.alert360.controller.dto.PreuveResolutionResponseDto;
import com.alert360.entity.AgentStructure;
import com.alert360.entity.PreuveResolution;
import org.springframework.stereotype.Component;

@Component

public class PreuveResolutionResponseMapper {

    public PreuveResolutionResponseDto toDto(PreuveResolution preuve){
        if(preuve==null) return null;

        PreuveResolutionResponseDto dto = new PreuveResolutionResponseDto();

        dto.setIdPreuve(preuve.getIdPreuve());
        dto.setTypePreuve(preuve.getTypePreuve());
        dto.setPhotoApresUrl(preuve.getPhotoApresUrl());
        dto.setRapportTexte(preuve.getRapportTexte());
        dto.setDateResolution(preuve.getDateResolution());

        if(preuve.getSignalement() != null){
            dto.setIdSignalement(preuve.getSignalement().getIdSignalement());
        }

        AgentStructure agent = preuve.getAgentEmetteur();
        if (agent == null && preuve.getSignalement() != null) {
            agent = preuve.getSignalement().getAgentAssigne();
        }
        if (agent != null) {
            dto.setNomAgent(agent.getNom());
            dto.setPrenomAgent(agent.getPrenom());
            dto.setTelephoneAgent(agent.getTelephone());
            dto.setMatriculeAgent(agent.getMatriculeAgent());
        }

        return  dto;

    }
}
