package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumTypeStructure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StructureCompetenteRequestDto {

    @NotBlank(message = "Le nom de la structure est obligatoire")
    private String nomStructure;

    @NotBlank(message = "Le quartier est obligatoire")
    private String quartier;

    @NotNull(message = "Le type de structure est obligatoire")
    private EnumTypeStructure typeStructure;



    // Représentation textuelle WKT du polygone PostGIS (ex: "POLYGON((longitude latitude, ...))")
    @NotBlank(message = "La zone de couverture GPS (format WKT) est obligatoire")
    private String zoneCouvertureGPS;

    private String telephoneUrgence;
}