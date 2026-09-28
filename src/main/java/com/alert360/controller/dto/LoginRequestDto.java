package com.alert360.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LoginRequestDto {

    @NotBlank(message = "Le téléphone ou l'identifiant est obligatoire")
    @Pattern(regexp = "^[0-9]{8,15}$", message = "Format de numéro de téléphone invalide")
    private String telephone;

    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    private String motDePasse;
}
