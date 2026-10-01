package com.alert360.mapper.Response;

import com.alert360.controller.dto.StructureCompetenteResponseDto;
import com.alert360.entity.StructureCompetente;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.stereotype.Component;

@Component
public class StructureCompetenteResponseMapper {

    private final WKTWriter wktWriter = new WKTWriter();

    public StructureCompetenteResponseDto toDto(StructureCompetente structure) {
        if (structure == null) {
            return null;
        }

        StructureCompetenteResponseDto dto = new StructureCompetenteResponseDto();

        dto.setIdStructure(structure.getIdStructure());
        dto.setNomStructure(structure.getNomStructure());
        dto.setQuartier(structure.getQuartier());
        dto.setTypeStructure(structure.getTypeStructure());
        dto.setTelephoneUrgence(structure.getTelephoneUrgence());

        // Conversion de l'objet Geometry PostGIS en chaîne de caractères WKT
        if (structure.getZoneCouvertureGPS() != null) {
            dto.setZoneCouvertureGPS(wktWriter.write(structure.getZoneCouvertureGPS()));
        }

        // Calcul des métriques associées
        dto.setNombreAgents(structure.getAgents() != null ? structure.getAgents().size() : 0);
        dto.setNombreSignalementsAssignes(
                structure.getSignalementsAssignes() != null ? structure.getSignalementsAssignes().size() : 0
        );

        return dto;
    }
}