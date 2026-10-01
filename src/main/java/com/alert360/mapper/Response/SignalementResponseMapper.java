package com.alert360.mapper.Response;

import com.alert360.controller.dto.SignalementResponseDto;
import com.alert360.entity.Signalement;
import org.springframework.stereotype.Component;

@Component
public class SignalementResponseMapper {

    public SignalementResponseDto toDto(Signalement signalement) {
        if (signalement == null) {
            return null;
        }

        SignalementResponseDto dto = new SignalementResponseDto();

        dto.setIdSignalement(signalement.getIdSignalement());
        dto.setCodeTrackingUnique(signalement.getCodeTrackingUnique());
        dto.setTypeUrgence(signalement.getTypeUrgence());
        dto.setStatut(signalement.getStatut());

        dto.setPhotoAvantUrl(signalement.getPhotoAvantUrl());
        dto.setAudioUrl(signalement.getAudioUrl());
        dto.setDescription(signalement.getDescription());
        dto.setRepereVisuel(signalement.getRepereVisuel());
        dto.setDateHeureAlerte(signalement.getDateHeureAlerte());

        //Extraction des coordonnées GPS à partir de l'objet PostGIS Point (SRID 4326)
        if (signalement.getLocalisation() != null) {
            dto.setLatitudeGPS(signalement.getLocalisation().getY());  // Y = Latitude
            dto.setLongitudeGPS(signalement.getLocalisation().getX()); // X = Longitude
        }

        // --- RELATIONS ---

        if (signalement.getCitoyen() != null) {
            dto.setCitoyenId(signalement.getCitoyen().getIdUtilisateur());
            dto.setCitoyenNomComplet(signalement.getCitoyen().getPrenom() + " " + signalement.getCitoyen().getNom());
        }

        if (signalement.getCategorie() != null) {
            dto.setCategorieId(signalement.getCategorie().getIdCategorie());
            dto.setCategorieNom(signalement.getCategorie().getNom());
        }

        if (signalement.getStructureAssignee() != null) {
            dto.setStructureAssigneeId(signalement.getStructureAssignee().getIdStructure());
            dto.setStructureAssigneeNom(signalement.getStructureAssignee().getNomStructure());
        }

        return dto;
    }
}