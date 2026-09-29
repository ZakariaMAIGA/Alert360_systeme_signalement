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

    private String zoneCouvertureGPS;

    private String telephoneUrgence;


}