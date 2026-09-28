package com.alert360.mapper;

import com.alert360.controller.dto.ActualiteRequestDto;
import com.alert360.entity.Actualite;
import com.alert360.entity.Admin;
import org.springframework.stereotype.Component;

@Component
public class ActualiteRequestMapper {

    public Actualite toEntity(
            ActualiteRequestDto dto,
            Admin auteur
    ) {

        Actualite actualite = new Actualite();

        actualite.setTitre(dto.getTitre());
        actualite.setCorpsTexte(dto.getCorpsTexte());
        actualite.setCommuneCible(dto.getCommuneCible());
        actualite.setEstUrgent(dto.getEstUrgent());

        actualite.setAuteur(auteur);

        return actualite;
    }
}