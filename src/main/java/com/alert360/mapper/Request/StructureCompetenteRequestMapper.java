package com.alert360.mapper.Request;

import com.alert360.controller.dto.StructureCompetenteRequestDto;
import com.alert360.entity.StructureCompetente;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Component;

@Component
public class StructureCompetenteRequestMapper {

    private final WKTReader wktReader = new WKTReader();

    public StructureCompetente toEntity(StructureCompetenteRequestDto dto) {
        if (dto == null) {
            return null;
        }

        StructureCompetente structure = new StructureCompetente();

        structure.setNomStructure(dto.getNomStructure());
        structure.setQuartier(dto.getQuartier());
        structure.setTypeStructure(dto.getTypeStructure());
        structure.setTelephoneUrgence(dto.getTelephoneUrgence());

        // Conversion de la chaîne WKT (String) vers l'objet Geometry PostGIS (SRID 4326)
        if (dto.getZoneCouvertureGPS() != null && !dto.getZoneCouvertureGPS().isBlank()) {
            try {
                Geometry geometry = wktReader.read(dto.getZoneCouvertureGPS());
                geometry.setSRID(4326); // Important pour la géolocalisation PostGIS
                structure.setZoneCouvertureGPS(geometry);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Le format WKT de la zone de couverture GPS est invalide : " + dto.getZoneCouvertureGPS(), e);
            }
        }

        return structure;
    }
}