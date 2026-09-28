package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private Long idUtilisateur;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private EnumRole role;
    private String quartier;
    private Boolean estActif;
}
