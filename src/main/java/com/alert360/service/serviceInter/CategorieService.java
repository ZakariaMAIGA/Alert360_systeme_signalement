package com.alert360.service;

import com.alert360.controller.dto.CategorieRequestDto;
import com.alert360.controller.dto.CategorieResponseDto;

import java.util.List;

public interface CategorieService {

    CategorieResponseDto creerCategorie(CategorieRequestDto dto);

    List<CategorieResponseDto> getAllCategories();

    CategorieResponseDto getCategorieById(Long id);

    CategorieResponseDto modifierCategorie(
            Long id,
            CategorieRequestDto dto
    );

    void supprimerCategorie(Long id);
}