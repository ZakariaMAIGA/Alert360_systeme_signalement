package com.alert360.service.serviceInter;

import com.alert360.controller.dto.CitoyenRequestDto;
import com.alert360.controller.dto.CitoyenResponseDto;

import java.util.List;

public interface CitoyenService {

    CitoyenResponseDto creerCitoyen(CitoyenRequestDto dto);

    CitoyenResponseDto modifierCitoyen(Long idUtilisateur, CitoyenRequestDto dto);

    CitoyenResponseDto obtenirParId(Long idUtilisateur);

    List<CitoyenResponseDto> obtenirTousLesCitoyens();

    List<CitoyenResponseDto> obtenirParQuartier(String quartier);

    void supprimerCitoyen(Long idUtilisateur);
}
