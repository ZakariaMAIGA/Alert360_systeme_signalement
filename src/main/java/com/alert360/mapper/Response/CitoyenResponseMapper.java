package com.alert360.mapper.Response;


import com.alert360.controller.dto.CitoyenResponseDto;
import com.alert360.entity.Citoyen;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CitoyenResponseMapper {
    public CitoyenResponseDto toDto(Citoyen entity) {
        if (entity == null) return null;

        CitoyenResponseDto dto = new CitoyenResponseDto();
        dto.setIdUtilisateur(entity.getIdUtilisateur());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setTelephone(entity.getTelephone());
        dto.setEmail(entity.getEmail());
        dto.setQuartier(entity.getQuartier());
        dto.setPointScore(entity.getPointScore());
        dto.setNombreSignalementsInvalides(entity.getNombreSignalementsInvalides());
        dto.setBadgesCiviques(entity.getBadgesCiviques() != null ? new ArrayList<>(entity.getBadgesCiviques()) : new ArrayList<>());
        dto.setRole(entity.getRole());
        dto.setEstActif(entity.getEstActif());
        dto.setDateCreation(entity.getDateCreation());
        return dto;
    }
}
