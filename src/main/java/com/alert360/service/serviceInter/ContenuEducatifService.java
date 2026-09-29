package com.alert360.service.serviceInter;

import com.alert360.controller.dto.ContenuEducatifRequestDto;
import com.alert360.controller.dto.ContenuEducatifResponseDto;

import java.util.List;

public interface ContenuEducatifService {

    ContenuEducatifResponseDto creerContenu(ContenuEducatifRequestDto dto);

    ContenuEducatifResponseDto modifierContenu(Long idContenu, ContenuEducatifRequestDto dto);

    ContenuEducatifResponseDto obetnirParId(Long idContenu);

    List<ContenuEducatifResponseDto> obtenirTousLesContenus();

    void supprimerContenu(Long idContenu);
}
