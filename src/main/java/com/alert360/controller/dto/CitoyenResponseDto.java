package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data

public class CitoyenResponseDto {

        private Long idUtilisateur;
        private String nom;
        private String prenom;
        private String telephone;
        private String email;
        private String quartier;
        private Integer pointScore;
        private Integer nombreSignalementsInvalides;
        private List<String> badgesCiviques;
        private EnumRole role;
        private Boolean estActif;
        private LocalDateTime dateCreation;

}
