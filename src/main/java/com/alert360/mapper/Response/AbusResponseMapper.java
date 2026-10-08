package com.alert360.mapper.Response;

import com.alert360.controller.dto.AbusResponseDto;
import com.alert360.entity.Abus;
import org.springframework.stereotype.Component;

@Component
public class AbusResponseMapper {
    public AbusResponseDto toDto(Abus abus) {
        AbusResponseDto dto = new AbusResponseDto();
        dto.setIdAbus(abus.getIdAbus());
        dto.setTypeAbus(abus.getTypeAbus());
        dto.setTypeAbusPersonnalise(abus.getTypeAbusPersonnalise());
        dto.setNiveauGravite(abus.getNiveauGravite());
        dto.setStatut(abus.getStatut());
        dto.setJustification(abus.getJustification());
        dto.setPieceJointeUrl(abus.getPieceJointeUrl());
        dto.setDateClassement(abus.getDateClassement());
        dto.setNomStructure(abus.getStructure().getNomStructure());

        var signalement = abus.getSignalement();
        dto.setIdSignalement(signalement.getIdSignalement());
        dto.setCodeTrackingUnique(signalement.getCodeTrackingUnique());
        dto.setCategorie(signalement.getCategorie() == null ? null : signalement.getCategorie().getNom());
        if (signalement.getCitoyen() != null) {
            dto.setCitoyenNomComplet(
                    (signalement.getCitoyen().getPrenom() + " " + signalement.getCitoyen().getNom()).trim()
            );
        }
        return dto;
    }
}
