package com.alert360.service;

import com.alert360.controller.dto.ContenuEducatifRequestDto;
import com.alert360.controller.dto.ContenuEducatifResponseDto;

import java.util.List;

public interface ContenuEducatifService {

    ContenuEducatifResponseDto creerContenu(
            ContenuEducatifRequestDto dto
    );

    List<ContenuEducatifResponseDto> getAllContenus();

    ContenuEducatifResponseDto getContenuById(Long id);

    ContenuEducatifResponseDto modifierContenu(
            Long id,
            ContenuEducatifRequestDto dto
    );

    void supprimerContenu(Long id);
}