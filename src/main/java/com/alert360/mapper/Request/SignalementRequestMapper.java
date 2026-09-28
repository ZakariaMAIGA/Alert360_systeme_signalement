package com.alert360.mapper;

import com.alert360.controller.dto.SignalementRequestDto;
import com.alert360.entity.Categorie;
import com.alert360.entity.Citoyen;
import com.alert360.entity.Signalement;
import org.springframework.stereotype.Component;

@Component
public class SignalementRequestMapper {

    public Signalement toEntity(
            SignalementRequestDto dto,
            Citoyen citoyen,
            Categorie categorie
    ) {

        Signalement signalement = new Signalement();

        signalement.setTypeUrgence(dto.getTypeUrgence());
        signalement.setPhotoAvantUrl(dto.getPhotoAvantUrl());
        signalement.setAudioUrl(dto.getAudioUrl());
        signalement.setDescription(dto.getDescription());
        signalement.setLatitudeGPS(dto.getLatitudeGPS());
        signalement.setLongitudeGPS(dto.getLongitudeGPS());
        signalement.setRepereVisuel(dto.getRepereVisuel());

        signalement.setCitoyen(citoyen);
        signalement.setCategorie(categorie);

        return signalement;
    }
}