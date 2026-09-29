package com.alert360.mapper.Response;

import com.alert360.controller.dto.CategorieResponseDto;
import com.alert360.entity.Categorie;
import org.springframework.stereotype.Component;

@Component
public class CategorieResponseMapper {

    public CategorieResponseDto toDto(Categorie categorie) {
        if (categorie==null) return null;
        CategorieResponseDto dto = new CategorieResponseDto();

        dto.setIdCategorie(categorie.getIdCategorie());
        dto.setNom(categorie.getNom());

        return dto;
    }
}