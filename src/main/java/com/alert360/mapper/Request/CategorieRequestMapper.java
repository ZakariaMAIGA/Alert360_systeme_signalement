package com.alert360.mapper.Request;

import com.alert360.controller.dto.CategorieRequestDto;
import com.alert360.entity.Categorie;
import org.springframework.stereotype.Component;

@Component
public class CategorieRequestMapper {

    public Categorie toEntity(CategorieRequestDto dto) {

        if (dto == null) return null;
        Categorie categorie = new Categorie();

        categorie.setNom(dto.getNom());

        return categorie;
    }
}