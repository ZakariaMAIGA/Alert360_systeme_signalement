package com.alert360.service.serviceInter;

import com.alert360.controller.dto.ActualiteRequestDto;
import com.alert360.controller.dto.ActualiteResponseDto;

import java.util.List;

public interface ActualiteService {

    ActualiteResponseDto publierActualite(ActualiteRequestDto dto);

    ActualiteResponseDto modifierActualite(Long idActualite, ActualiteRequestDto dto);

    List<ActualiteResponseDto> obtenirToutesLesActualites();

    void supprimerActualite(Long idActualite);
}
