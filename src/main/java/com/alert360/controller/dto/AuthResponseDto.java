package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {

    private String token;

    @Builder.Default
    private String type = "Bearer";

    private Long idUtilisateur;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private EnumRole role;
    private String quartier;
    private Boolean estActif;

    // Champs spécifiques aux agents de structure
    private Long idStructure;
    private Boolean estResponsable;
}