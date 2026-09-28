package com.alert360.mapper;

import com.alert360.controller.dto.SignalementResponseDto;
import com.alert360.entity.Signalement;
import org.springframework.stereotype.Component;

@Component
public class SignalementResponseMapper {

    public SignalementResponseDto toDto(Signalement signalement) {

        SignalementResponseDto dto = new SignalementResponseDto();

        dto.setIdSignalement(signalement.getIdSignalement());
        dto.setCodeTrackingUnique(signalement.getCodeTrackingUnique());
        dto.setTypeUrgence(signalement.getTypeUrgence());
        dto.setStatut(signalement.getStatut());

        dto.setPhotoAvantUrl(signalement.getPhotoAvantUrl());
        dto.setAudioUrl(signalement.getAudioUrl());
        dto.setDescription(signalement.getDescription());

        dto.setLatitudeGPS(signalement.getLatitudeGPS());
        dto.setLongitudeGPS(signalement.getLongitudeGPS());
        dto.setRepereVisuel(signalement.getRepereVisuel());

        dto.setDateHeureAlerte(signalement.getDateHeureAlerte());

        if (signalement.getCitoyen() != null) {
            dto.setCitoyenId(signalement.getCitoyen().getIdCitoyen());
        }

        if (signalement.getCategorie() != null) {
            dto.setCategorieId(signalement.getCategorie().getIdCategorie());
        }

        if (signalement.getStructureAssignee() != null) {
            dto.setStructureAssigneeId(
                signalement.getStructureAssignee().getIdStructure()
            );
        }

        if (signalement.getPreuveResolution() != null) {
            dto.setPreuveResolutionId(
                signalement.getPreuveResolution().getIdPreuve()
            );
        }

        return dto;
    }
}