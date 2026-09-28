package com.alert360.mapper;

import com.alert360.controller.dto.StructureCompetenteRequestDto;
import com.alert360.entity.StructureCompetente;
import org.springframework.stereotype.Component;

@Component
public class StructureCompetenteRequestMapper {

    public StructureCompetente toEntity(StructureCompetenteRequestDto dto) {

        StructureCompetente structure = new StructureCompetente();

        structure.setNomStructure(dto.getNomStructure());
        structure.setQuartier(dto.getQuartier());
        structure.setTypeStructure(dto.getTypeStructure());
        structure.setZoneCouvertureGPS(dto.getZoneCouvertureGPS());
        structure.setTelephoneUrgence(dto.getTelephoneUrgence());

        return structure;
    }
}