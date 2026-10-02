// SosServiceImpl.java
package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.SosResponseDto;
import com.alert360.entity.enums.EnumTypeStructure;
import com.alert360.repository.StructureCompetenteRepository;
import com.alert360.service.serviceInter.SosService;
import com.alert360.util.GeometryUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SosServiceImpl implements SosService {

    private final StructureCompetenteRepository structureCompetenteRepository;

    @Override
    public List<SosResponseDto> obtenirStructuresUrgenceProches(Double latitude, Double longitude, Double rayonKm) {

        // 1. Convertir les coordonnées GPS du citoyen en Point JTS (Longitude, Latitude)
        Point pointCitoyen = GeometryUtil.createPoint(latitude, longitude);

        // 2. Définir le rayon de recherche en mètres (par défaut 15 km si non précisé)
        double rayonMetres = (rayonKm != null && rayonKm > 0) ? rayonKm * 1000 : 15000.0;

        // 3. Récupérer les structures ordonnées par proximité
        List<StructureCompetenteRepository.SosStructureProjection> projections =
                structureCompetenteRepository.findNearbyStructuresForSos(pointCitoyen, rayonMetres);

        // 4. Mapper vers le DTO de réponse
        return projections.stream()
                .map(p -> SosResponseDto.builder()
                        .idStructure(p.getIdStructure())
                        .nomStructure(p.getNomStructure())
                        .quartier(p.getQuartier())
                        .typeStructure(EnumTypeStructure.valueOf(p.getTypeStructure()))
                        .telephoneUrgence(p.getTelephoneUrgence())
                        .distanceEnMetres(Math.round(p.getDistanceEnMetres() * 100.0) / 100.0) // Arrondi à 2 décimales
                        .build())
                .toList();
    }
}