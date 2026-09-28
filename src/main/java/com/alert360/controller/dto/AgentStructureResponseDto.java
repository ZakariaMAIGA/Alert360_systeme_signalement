package com.alert360.controller.dto;


import lombok.Data;

@Data
public class AgentStructureResponseDto {
    private Long idUtilisateur;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String matriculeAgent;
    private Boolean estResponsable;
    private Long idStructure;
    private String nomStructure;
}
