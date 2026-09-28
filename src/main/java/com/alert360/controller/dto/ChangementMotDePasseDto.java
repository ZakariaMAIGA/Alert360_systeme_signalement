package com.alert360.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangementMotDePasseDto {
    @NotBlank(message = "L'ancien mot de passe est obligatoire")
    private String ancienMotDePasse;

    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    private String nouveauMotDePasse;
}
