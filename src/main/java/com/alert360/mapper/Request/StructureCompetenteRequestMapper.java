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

        structure.setZoneCouvertureGPS(
                convertirWktEnGeometry(dto.getZoneCouvertureGPS())
        );

        return structure;
    }

    /**
     * Convertit une chaîne WKT en Geometry JTS avec SRID 4326.
     */
    private Geometry convertirWktEnGeometry(String wkt) {

        if (wkt == null || wkt.isBlank()) {
            return null;
        }

        try {
            Geometry geometry = wktReader.read(wkt);

            // SRID utilisé pour les coordonnées GPS
            geometry.setSRID(4326);

            return geometry;

        } catch (ParseException e) {

            throw new IllegalArgumentException(
                    "Le format WKT de la zone de couverture GPS est invalide : " + wkt,
                    e
            );
        }
    }
}