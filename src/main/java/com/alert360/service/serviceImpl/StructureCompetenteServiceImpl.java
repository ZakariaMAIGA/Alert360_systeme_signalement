package com.alert360.service;

import com.alert360.controller.dto.StructureCompetenteRequestDto;
import com.alert360.controller.dto.StructureCompetenteResponseDto;
import com.alert360.entity.StructureCompetente;
import com.alert360.mapper.StructureCompetenteRequestMapper;
import com.alert360.mapper.StructureCompetenteResponseMapper;
import com.alert360.repository.StructureCompetenteRepository;
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


    @Override
    public StructureCompetenteResponseDto creerStructure(
            StructureCompetenteRequestDto dto
    ) {

        StructureCompetente structure =
                structureCompetenteRequestMapper.toEntity(dto);

        StructureCompetente structureEnregistree =
                structureCompetenteRepository.save(structure);

        return structureCompetenteResponseMapper.toDto(
                structureEnregistree
        );
    }


    @Override
    public List<StructureCompetenteResponseDto> getAllStructures() {

        return structureCompetenteRepository.findAll()
                .stream()
                .map(structureCompetenteResponseMapper::toDto)
                .toList();
    }


    @Override
    public StructureCompetenteResponseDto getStructureById(Long id) {

        StructureCompetente structure =
                structureCompetenteRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Structure compétente introuvable avec l'id : "
                                                + id
                                )
                        );

        return structureCompetenteResponseMapper.toDto(structure);
    }


    @Override
    public StructureCompetenteResponseDto modifierStructure(
            Long id,
            StructureCompetenteRequestDto dto
    ) {

        StructureCompetente structure =
                structureCompetenteRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Structure compétente introuvable avec l'id : "
                                                + id
                                )
                        );

        structure.setNomStructure(dto.getNomStructure());
        structure.setQuartier(dto.getQuartier());
        structure.setTypeStructure(dto.getTypeStructure());
        structure.setZoneCouvertureGPS(dto.getZoneCouvertureGPS());
        structure.setTelephoneUrgence(dto.getTelephoneUrgence());

        StructureCompetente structureModifiee =
                structureCompetenteRepository.save(structure);

        return structureCompetenteResponseMapper.toDto(
                structureModifiee
        );
    }


    @Override
    public void supprimerStructure(Long id) {

        if (!structureCompetenteRepository.existsById(id)) {
            throw new RuntimeException(
                    "Structure compétente introuvable avec l'id : " + id
            );
        }

        structureCompetenteRepository.deleteById(id);
    }
}