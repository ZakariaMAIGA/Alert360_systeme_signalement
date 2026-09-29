package com.alert360.service.serviceInter;

import com.alert360.controller.dto.StructureCompetenteRequestDto;
import com.alert360.controller.dto.StructureCompetenteResponseDto;
import com.alert360.entity.enums.EnumTypeStructure;

import java.util.List;

public interface StructureCompetenteService {

    StructureCompetenteResponseDto creerStructure(StructureCompetenteRequestDto dto);

    StructureCompetenteResponseDto modifierStructure(Long idStructure, StructureCompetenteRequestDto dto);

    StructureCompetenteResponseDto obtenirParId(Long idStructure);

    List<StructureCompetenteResponseDto> obtenirToutesLesStructures();

    List<StructureCompetenteResponseDto> obtenirPartype(EnumTypeStructure typeStructure);

    void supprimerStructre(Long idStructure);
}
