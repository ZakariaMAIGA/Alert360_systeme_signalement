package com.alert360.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualiteRequestDto {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "Le corps du texte est obligatoire")
    private String corpsTexte;

    private String communeCible;

    @NotNull(message = "Le caractère urgent est obligatoire")
    private Boolean estUrgent;

    @NotNull(message = "L'auteur est obligatoire")
    private Long auteurId;
}