package com.alert360.mapper.Response;

import com.alert360.controller.dto.ActualiteResponseDto;
import com.alert360.entity.Actualite;
import org.springframework.stereotype.Component;

@Component
public class ActualiteResponseMapper {

    public ActualiteResponseDto toDto(Actualite actualite) {

        ActualiteResponseDto dto = new ActualiteResponseDto();

        dto.setIdActualite(actualite.getIdActualite());
        dto.setTitre(actualite.getTitre());
        dto.setCorpsTexte(actualite.getCorpsTexte());
        dto.setCommuneCible(actualite.getCommuneCible());
        dto.setEstUrgent(actualite.getEstUrgent());
        dto.setDatePublication(actualite.getDatePublication());

        if (actualite.getAuteur() != null) {
            dto.setIdActualite(actualite.getAuteur().getIdUtilisateur());
        }

        return dto;
    }
}