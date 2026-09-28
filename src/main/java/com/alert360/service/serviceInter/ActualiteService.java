package com.alert360.service;

import com.alert360.controller.dto.ActualiteRequestDto;
import com.alert360.controller.dto.ActualiteResponseDto;

import java.util.List;

public interface ActualiteService {

    ActualiteResponseDto creerActualite(
            ActualiteRequestDto dto
    );

    List<ActualiteResponseDto> getAllActualites();

    ActualiteResponseDto getActualiteById(Long id);

    ActualiteResponseDto modifierActualite(
            Long id,
            ActualiteRequestDto dto
    );

    void supprimerActualite(Long id);
}