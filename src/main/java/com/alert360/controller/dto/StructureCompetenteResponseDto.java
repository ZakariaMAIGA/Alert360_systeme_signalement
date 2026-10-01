package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumTypeStructure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StructureCompetenteResponseDto {

    private Long idStructure;

    private String nomStructure;

    private String quartier;

    private EnumTypeStructure typeStructure;

    // Représentation textuelle (WKT / GeoJSON) du périmètre géospatiale
    private String zoneCouvertureGPS;

    private String telephoneUrgence;

    private int nombreAgents;

    private int nombreSignalementsAssignes;
}