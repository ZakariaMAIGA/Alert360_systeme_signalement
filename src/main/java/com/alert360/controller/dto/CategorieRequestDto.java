package com.alert360.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategorieRequestDto {

    @NotBlank(message = "Le nom de la catégorie est obligatoire")
    private String nom;
}