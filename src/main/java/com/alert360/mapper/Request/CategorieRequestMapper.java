package com.alert360.mapper;

import com.alert360.controller.dto.CategorieRequestDto;
import com.alert360.entity.Categorie;
import org.springframework.stereotype.Component;

@Component
public class CategorieRequestMapper {

    public Categorie toEntity(CategorieRequestDto dto) {

        Categorie categorie = new Categorie();

        categorie.setNom(dto.getNom());

        return categorie;
    }
}