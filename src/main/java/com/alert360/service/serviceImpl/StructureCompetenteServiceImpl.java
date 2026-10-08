package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.StructureCompetenteRequestDto;
import com.alert360.controller.dto.StructureCompetenteResponseDto;
import com.alert360.entity.StructureCompetente;
import com.alert360.entity.enums.EnumTypeStructure;
import com.alert360.mapper.Request.StructureCompetenteRequestMapper;
import com.alert360.mapper.Response.StructureCompetenteResponseMapper;
import com.alert360.repository.StructureCompetenteRepository;
import com.alert360.service.serviceInter.StructureCompetenteService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StructureCompetenteServiceImpl implements StructureCompetenteService {

    private final StructureCompetenteRepository structureCompetenteRepository;
    private final StructureCompetenteRequestMapper structureCompetenteRequestMapper;
    private final StructureCompetenteResponseMapper structureCompetenteResponseMapper;

    // ==========================================
    // CREER UNE STRUCTURE
    // ==========================================
    @Override
    @Transactional
    public StructureCompetenteResponseDto creerStructure(StructureCompetenteRequestDto dto) {
        // 1. Transformer le DTO en Entity (gestion de la géométrie gérée dans le mapper)
        StructureCompetente structure = structureCompetenteRequestMapper.toEntity(dto);

        // 2. Enregistrer dans la base de données
        StructureCompetente structureEnregistree = structureCompetenteRepository.save(structure);

        // 3. Transformer Entity -> Response DTO
        return structureCompetenteResponseMapper.toDto(structureEnregistree);
    }

    // ==========================================
    // MODIFIER UNE STRUCTURE
    // ==========================================
    @Override
    @Transactional
    public StructureCompetenteResponseDto modifierStructure(Long idStructure, StructureCompetenteRequestDto dto) {
        StructureCompetente structure =
                structureCompetenteRepository.findById(idStructure)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Structure compétente introuvable avec l'id : "
                                                + idStructure
                                )
                        );

        structure.setNomStructure(dto.getNomStructure());
        structure.setQuartier(dto.getQuartier());
        structure.setTypeStructure(dto.getTypeStructure());
        structure.setTelephoneUrgence(dto.getTelephoneUrgence());

        /*
         * Conversion WKT → Geometry centralisée dans le mapper.
         */
        StructureCompetente structureAvecZone =
                structureCompetenteRequestMapper.toEntity(dto);

        structure.setZoneCouvertureGPS(
                structureAvecZone.getZoneCouvertureGPS()
        );

        StructureCompetente structureModifiee =
                structureCompetenteRepository.save(structure);

        return structureCompetenteResponseMapper.toDto(
                structureModifiee
        );
    }

    // ==========================================
    // OBTENIR UNE STRUCTURE PAR ID
    // ==========================================
    @Override
    @Transactional(readOnly = true)
    public StructureCompetenteResponseDto obtenirParId(Long idStructure) {
        StructureCompetente structure = structureCompetenteRepository.findById(idStructure)
                .orElseThrow(() -> new RuntimeException("Structure compétente introuvable avec l'id : " + idStructure));

        return structureCompetenteResponseMapper.toDto(structure);
    }

    // ==========================================
    // OBTENIR TOUTES LES STRUCTURES
    // ==========================================
    @Override
    @Transactional(readOnly = true)
    public List<StructureCompetenteResponseDto> obtenirToutesLesStructures() {
        return structureCompetenteRepository.findAll()
                .stream()
                .map(structureCompetenteResponseMapper::toDto)
                .toList();
    }

    // ==========================================
    // OBTENIR LES STRUCTURES PAR TYPE
    // ==========================================
    @Override
    @Transactional(readOnly = true)
    public List<StructureCompetenteResponseDto> obtenirParType(EnumTypeStructure typeStructure) {
        return structureCompetenteRepository.findByTypeStructure(typeStructure)
                .stream()
                .map(structureCompetenteResponseMapper::toDto)
                .toList();
    }

    // ==========================================
    // SUPPRIMER UNE STRUCTURE
    // ==========================================
    @Override
    @Transactional
    public void supprimerStructure(Long idStructure) {
        if (!structureCompetenteRepository.existsById(idStructure)) {
            throw new RuntimeException("Structure compétente introuvable avec l'id : " + idStructure);
        }
        structureCompetenteRepository.deleteById(idStructure);
    }
}