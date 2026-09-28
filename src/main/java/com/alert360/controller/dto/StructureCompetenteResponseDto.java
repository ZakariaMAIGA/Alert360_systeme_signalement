package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumTypeStructure;
import lombok.Data;

@Data
public class StructureCompetenteResponseDto {

    private Long idStructure;

    private String nomStructure;

    private String quartier;

    private EnumTypeStructure typeStructure;

    private String zoneCouvertureGPS;

    private String telephoneUrgence;
}