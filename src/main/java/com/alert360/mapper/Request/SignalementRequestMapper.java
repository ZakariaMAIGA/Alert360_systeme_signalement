package com.alert360.mapper.Request;

import com.alert360.controller.dto.SignalementRequestDto;
import com.alert360.entity.Categorie;
import com.alert360.entity.Citoyen;
import com.alert360.entity.Signalement;
import com.alert360.util.GeometryUtil;
import org.springframework.stereotype.Component;

@Component
public class SignalementRequestMapper {

    public Signalement toEntity(SignalementRequestDto dto, Citoyen citoyen, Categorie categorie) {
        if (dto == null) {
            return null;
        }

        Signalement signalement = new Signalement();

        signalement.setTypeUrgence(dto.getTypeUrgence());
        signalement.setDescription(dto.getDescription());
        signalement.setPhotoAvantUrl(dto.getPhotoAvantUrl());
        signalement.setAudioUrl(dto.getAudioUrl());
        signalement.setRepereVisuel(dto.getRepereVisuel());

        //Conversion des coordonnées GPS décimales en Point PostGIS (SRID 4326)
        signalement.setLocalisation(GeometryUtil.createPoint(dto.getLatitudeGPS(), dto.getLongitudeGPS()));

        // Relations obligatoires injectées depuis le service
        signalement.setCitoyen(citoyen);
        signalement.setCategorie(categorie);

        // Note: codeTrackingUnique, dateHeureAlerte et statut (DECLARE)
        // sont gérés automatiquement via @PrePersist dans l'entité Signalement.
        // La structureAssignee sera quant à elle déterminée automatiquement par PostGIS dans le Service.

        return signalement;
    }
}