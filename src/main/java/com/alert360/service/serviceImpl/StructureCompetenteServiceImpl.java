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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StructureCompetenteServiceImpl
        implements StructureCompetenteService {

    private final StructureCompetenteRepository structureCompetenteRepository;

    private final StructureCompetenteRequestMapper structureCompetenteRequestMapper;

    private final StructureCompetenteResponseMapper structureCompetenteResponseMapper;


    // ==========================================
    // CREER UNE STRUCTURE
    // ==========================================
    @Override
    public StructureCompetenteResponseDto creerStructure(
            StructureCompetenteRequestDto dto
    ) {

        // 1. Transformer le DTO en Entity
        StructureCompetente structure =
                structureCompetenteRequestMapper.toEntity(dto);

        // 2. Enregistrer dans la base de données
        StructureCompetente structureEnregistree =
                structureCompetenteRepository.save(structure);

        // 3. Transformer Entity -> Response DTO
        return structureCompetenteResponseMapper.toDto(
                structureEnregistree
        );
    }


    // ==========================================
    // MODIFIER UNE STRUCTURE
    // ==========================================
    @Override
    public StructureCompetenteResponseDto modifierStructure(
            Long idStructure,
            StructureCompetenteRequestDto dto
    ) {

        // 1. Rechercher la structure
        StructureCompetente structure =
                structureCompetenteRepository.findById(idStructure)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Structure compétente introuvable avec l'id : "
                                                + idStructure
                                )
                        );

        // 2. Modifier les informations
        structure.setNomStructure(dto.getNomStructure());
        structure.setQuartier(dto.getQuartier());
        structure.setTypeStructure(dto.getTypeStructure());
        structure.setZoneCouvertureGPS(dto.getZoneCouvertureGPS());
        structure.setTelephoneUrgence(dto.getTelephoneUrgence());

        // 3. Enregistrer les modifications
        StructureCompetente structureModifiee =
                structureCompetenteRepository.save(structure);

        // 4. Transformer Entity -> DTO
        return structureCompetenteResponseMapper.toDto(
                structureModifiee
        );
    }


    // ==========================================
    // OBTENIR UNE STRUCTURE PAR ID
    // ==========================================
    @Override
    public StructureCompetenteResponseDto obtenirParId(
            Long idStructure
    ) {

        // 1. Rechercher la structure
        StructureCompetente structure =
                structureCompetenteRepository.findById(idStructure)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Structure compétente introuvable avec l'id : "
                                                + idStructure
                                )
                        );

        // 2. Transformer Entity -> DTO
        return structureCompetenteResponseMapper.toDto(
                structure
        );
    }


    // ==========================================
    // OBTENIR TOUTES LES STRUCTURES
    // ==========================================
    @Override
    public List<StructureCompetenteResponseDto>
    obtenirToutesLesStructures() {

        return structureCompetenteRepository.findAll()
                .stream()
                .map(structureCompetenteResponseMapper::toDto)
                .toList();
    }


    // ==========================================
    // OBTENIR LES STRUCTURES PAR TYPE
    // ==========================================
    @Override
    public List<StructureCompetenteResponseDto>
    obtenirPartype(
            EnumTypeStructure typeStructure
    ) {

        return structureCompetenteRepository
                .findByTypeStructure(typeStructure)
                .stream()
                .map(structureCompetenteResponseMapper::toDto)
                .toList();
    }


    // ==========================================
    // SUPPRIMER UNE STRUCTURE
    // ==========================================
    @Override
    public void supprimerStructre(
            Long idStructure
    ) {

        // 1. Vérifier que la structure existe
        if (!structureCompetenteRepository.existsById(idStructure)) {

            throw new RuntimeException(
                    "Structure compétente introuvable avec l'id : "
                            + idStructure
            );
        }

        // 2. Supprimer
        structureCompetenteRepository.deleteById(idStructure);
    }
}
