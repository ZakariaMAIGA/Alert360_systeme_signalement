package com.alert360.service;

import com.alert360.controller.dto.StructureCompetenteRequestDto;
import com.alert360.controller.dto.StructureCompetenteResponseDto;

import java.util.List;

public interface StructureCompetenteService {

    StructureCompetenteResponseDto creerStructure(
            StructureCompetenteRequestDto dto
    );

    List<StructureCompetenteResponseDto> getAllStructures();

    StructureCompetenteResponseDto getStructureById(Long id);

    StructureCompetenteResponseDto modifierStructure(
            Long id,
            StructureCompetenteRequestDto dto
    );

    void supprimerStructure(Long id);
}