package com.alert360.mapper.Response;

import com.alert360.controller.dto.ContenuEducatifResponseDto;
import com.alert360.entity.ContenuEducatif;
import org.springframework.stereotype.Component;

@Component
public class ContenuEducatifResponseMapper {

    public ContenuEducatifResponseDto toDto(ContenuEducatif contenu) {

        ContenuEducatifResponseDto dto = new ContenuEducatifResponseDto();

        dto.setIdContenu(contenu.getIdContenu());
        dto.setTitre(contenu.getTitre());
        dto.setTheme(contenu.getTheme());
        dto.setFormat(contenu.getFormat());
        dto.setMediaUrl(contenu.getMediaUrl());
        dto.setDatePublication(contenu.getDatePublication());

        if (contenu.getAuteur() != null) {
            dto.setAuteurId(contenu.getAuteur().getIdUtilisateur());
        }

        return dto;
    }
}