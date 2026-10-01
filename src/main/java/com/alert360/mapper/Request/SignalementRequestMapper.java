package com.alert360.mapper.Request;

import com.alert360.controller.dto.SignalementRequestDto;
import com.alert360.entity.Categorie;
import com.alert360.entity.Citoyen;
import com.alert360.entity.Signalement;
import com.alert360.util.GeometryUtil;
import org.springframework.stereotype.Component;

@Component
public class SignalementRequestMapper {

    /**
     * Convertit un SignalementRequestDto en entité Signalement.
     */
    public Signalement toEntity(SignalementRequestDto dto, Citoyen citoyen, Categorie categorie) {
        if (dto == null) {
            return null;
        }

        Signalement signalement = new Signalement();
        updateEntityFromDto(signalement, dto, citoyen, categorie);

        return signalement;
    }

    /**
     * Met à jour une entité Signalement existante avec les données du DTO.
     */
    public void updateEntityFromDto(Signalement entity, SignalementRequestDto dto, Citoyen citoyen, Categorie categorie) {
        if (entity == null || dto == null) {
            return;
        }

        entity.setTypeUrgence(dto.getTypeUrgence());
        entity.setDescription(dto.getDescription());
        entity.setPhotoAvantUrl(dto.getPhotoAvantUrl());
        entity.setAudioUrl(dto.getAudioUrl());
        entity.setRepereVisuel(dto.getRepereVisuel());

        // Conversion sécurisée des coordonnées GPS en Point PostGIS (SRID 4326)
        if (dto.getLatitudeGPS() != null && dto.getLongitudeGPS() != null) {
            entity.setLocalisation(GeometryUtil.createPoint(dto.getLatitudeGPS(), dto.getLongitudeGPS()));
        } else {
            entity.setLocalisation(null);
        }

        // Relations associées
        if (citoyen != null) {
            entity.setCitoyen(citoyen);
        }
        if (categorie != null) {
            entity.setCategorie(categorie);
        }
    }
}