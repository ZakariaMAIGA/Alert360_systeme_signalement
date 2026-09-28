package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminResponseDto {

    private Long idUtilisateur;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private EnumRole role;
    private Boolean estActif;
    private LocalDateTime dateCreation;

    // Statistiques d'activités éditoriales
    private Integer nombreActualitesPubliees;
    private Integer nombreContenusEducatifsPublies;
}
