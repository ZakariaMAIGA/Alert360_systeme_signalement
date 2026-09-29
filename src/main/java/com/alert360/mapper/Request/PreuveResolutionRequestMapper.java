package com.alert360.mapper.Request;

import com.alert360.controller.dto.PreuveResolutionRequestDto;
import com.alert360.entity.PreuveResolution;
import com.alert360.entity.Signalement;
import org.springframework.stereotype.Component;

@Component
public class PreuveResolutionRequestMapper {

    public PreuveResolution toEntity(PreuveResolutionRequestDto dto, Signalement signalement) {
        if (dto == null) return null;

        PreuveResolution preuve = new PreuveResolution();
        preuve.setTypePreuve(dto.getTypePreuve());
        preuve.setPhotoApresUrl(dto.getPhotoApresUrl());
        preuve.setRapportTexte(dto.getRapportTexte());
        preuve.setSignalement(signalement);

        return preuve;
    }
}