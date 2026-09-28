package com.alert360.controller.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class AgentStructureRequestDto {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;

    @NotBlank(message = "Le matricule de l'agent est obligatoire")
    private String matriculeAgent;

    private Boolean estResponsable = false;

    @NotNull(message = "L'identifiant de la structure est obligatoire")
    private Long idStructure;
}
