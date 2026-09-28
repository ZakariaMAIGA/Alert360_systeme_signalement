package com.alert360.mapper;

import com.alert360.controller.dto.ContenuEducatifRequestDto;
import com.alert360.entity.Admin;
import com.alert360.entity.ContenuEducatif;
import org.springframework.stereotype.Component;

@Component
public class ContenuEducatifRequestMapper {

    public ContenuEducatif toEntity(
            ContenuEducatifRequestDto dto,
            Admin auteur
    ) {

        ContenuEducatif contenu = new ContenuEducatif();

        contenu.setTitre(dto.getTitre());
        contenu.setTheme(dto.getTheme());
        contenu.setFormat(dto.getFormat());
        contenu.setMediaUrl(dto.getMediaUrl());

        contenu.setAuteur(auteur);

        return contenu;
    }
}