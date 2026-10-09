package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumStatut;
import com.alert360.entity.enums.EnumTypeUrgence;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignalementResponseDto {

    private Long idSignalement;
    private String codeTrackingUnique;
    private EnumTypeUrgence typeUrgence;
    private EnumStatut statut;
    private String description;

    // Coordonnées GPS réexpédiées depuis le Point PostGIS
    private Double latitudeGPS;
    private Double longitudeGPS;

    private String repereVisuel;
    private String photoAvantUrl;
    private String audioUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateHeureAlerte;

    // Informations du citoyen émetteur
    private Long citoyenId;
    private String citoyenNomComplet;

    // Informations de la catégorie
    private Long categorieId;
    private String categorieNom;

    // Informations de la structure assignée (peut être null si aucune structure à proximité)
    private Long structureAssigneeId;
    private String structureAssigneeNom;

    // Identifiant de l’agent de terrain auquel le signalement est attribué
    private Long agentAssigneId;
}