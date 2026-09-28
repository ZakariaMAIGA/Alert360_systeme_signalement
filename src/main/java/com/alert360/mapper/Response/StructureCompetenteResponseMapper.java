package com.alert360.mapper;

import com.alert360.controller.dto.StructureCompetenteResponseDto;
import com.alert360.entity.StructureCompetente;
import org.springframework.stereotype.Component;

@Component
public class StructureCompetenteResponseMapper {

    public StructureCompetenteResponseDto toDto(
            StructureCompetente structure) {

        StructureCompetenteResponseDto dto =
                new StructureCompetenteResponseDto();

        dto.setIdStructure(structure.getIdStructure());
        dto.setNomStructure(structure.getNomStructure());
        dto.setQuartier(structure.getQuartier());
        dto.setTypeStructure(structure.getTypeStructure());
        dto.setZoneCouvertureGPS(structure.getZoneCouvertureGPS());
        dto.setTelephoneUrgence(structure.getTelephoneUrgence());

        return dto;
    }
}