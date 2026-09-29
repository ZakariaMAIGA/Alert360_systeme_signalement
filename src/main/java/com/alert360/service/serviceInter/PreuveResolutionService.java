package com.alert360.service.serviceInter;

import com.alert360.controller.dto.PreuveResolutionRequestDto;
import com.alert360.controller.dto.PreuveResolutionResponseDto;

import java.util.List;

public interface PreuveResolutionService {
    PreuveResolutionResponseDto ajouterPreuveResolution(PreuveResolutionRequestDto dto);

    PreuveResolutionResponseDto modifierPreuveResolution(Long idPreuve, PreuveResolutionRequestDto dto);

    PreuveResolutionResponseDto obtenirParId(Long idPreuve);

    PreuveResolutionResponseDto obtenirParSignalement(Long idSignalement);

    List<PreuveResolutionResponseDto> obtenirToutesLesPreuves();

    void supprimerPreuve(Long idPreuve);
}
