package com.alert360.service.serviceInter;

import com.alert360.controller.dto.CategorieRequestDto;
import com.alert360.controller.dto.CategorieResponseDto;

import java.util.List;

public interface CategorieService {
    CategorieResponseDto creerCategorie(CategorieRequestDto dto);

    CategorieResponseDto modifierCategorie(Long idCategorie, CategorieRequestDto dto);

    CategorieResponseDto obtenirParId(Long idCategorie);

    List<CategorieResponseDto> obtenirToutesLesCategories();

    void supprimerCategorie(Long idCategorie);
}
