package com.alert360.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class  AgentStructureUpdateDto {

    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    private String prenom;

    @Email(message = "L'adresse email doit être valide")
    private String email;

    private String telephone;

    @NotBlank(message = "Le matricule ne peut pas être vide")
    private String matriculeAgent;

    private Boolean estResponsable;

    @NotNull(message = "L'identifiant de la structure est obligatoire")
    private Long idStructure;

}
