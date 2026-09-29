package com.alert360.controller.dto;


import com.alert360.entity.enums.EnumRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgentStructureResponseDto {
    private Long idUtilisateur;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String matriculeAgent;
    private boolean estResponsable;
    private EnumRole role;
    private Boolean estActif;
    private LocalDateTime dateCreation;
    private Long idStructure;
    private String nomStructure;
}
