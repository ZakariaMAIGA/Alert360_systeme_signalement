package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentStructureResponseDto {

    private Long idUtilisateur;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String matriculeAgent;

    @JsonProperty("estResponsable")
    private Boolean estResponsable;

    private EnumRole role;

    @JsonProperty("estActif")
    private Boolean estActif;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateCreation;

    private Long idStructure;
    private String nomStructure;
}