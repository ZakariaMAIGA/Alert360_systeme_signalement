package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumTypeUrgence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignalementRequestDto {

    @NotNull(message = "Le type d'urgence est obligatoire")
    private EnumTypeUrgence typeUrgence;

    private String photoAvantUrl;

    private String audioUrl;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "La latitude est obligatoire")
    private Double latitudeGPS;

    @NotNull(message = "La longitude est obligatoire")
    private Double longitudeGPS;

    private String repereVisuel;

    @NotNull(message = "Le citoyen est obligatoire")
    private Long citoyenId;

    @NotNull(message = "La catégorie est obligatoire")
    private Long categorieId;
}