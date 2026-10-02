package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumTypeStructure;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SosResponseDto {

    private Long idStructure;
    private String nomStructure;
    private String quartier;
    private EnumTypeStructure typeStructure;
    private String telephoneUrgence;
    private Double distanceEnMetres; // Distance entre le citoyen et la structure
}