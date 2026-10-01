package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumTypeUrgence;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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

    @NotNull(message = "La latitude GPS est obligatoire")
    @DecimalMin(value = "-90.0", message = "La latitude doit être supérieure ou égale à -90")
    @DecimalMax(value = "90.0", message = "La latitude doit être inférieure ou égale à 90")
    private Double latitudeGPS;

    @NotNull(message = "La longitude GPS est obligatoire")
    @DecimalMin(value = "-180.0", message = "La longitude doit être supérieure ou égale à -180")
    @DecimalMax(value = "180.0", message = "La longitude doit être inférieure ou égale à 180")
    private Double longitudeGPS;

    private String repereVisuel;

    @NotNull(message = "Le citoyen est obligatoire")
    private Long citoyenId;

    @NotNull(message = "La catégorie est obligatoire")
    private Long categorieId;
}