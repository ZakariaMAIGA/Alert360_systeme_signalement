package com.alert360.service;

import com.alert360.controller.dto.SignalementRequestDto;
import com.alert360.controller.dto.SignalementResponseDto;

import java.util.List;

public interface SignalementService {

    SignalementResponseDto creerSignalement(SignalementRequestDto dto);

    List<SignalementResponseDto> getAllSignalements();

    SignalementResponseDto getSignalementById(Long id);

    SignalementResponseDto modifierSignalement(
            Long id,
            SignalementRequestDto dto
    );

    void supprimerSignalement(Long id);
}