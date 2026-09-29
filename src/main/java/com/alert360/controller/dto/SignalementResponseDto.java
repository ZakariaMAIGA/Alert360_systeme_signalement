package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumStatut;
import com.alert360.entity.enums.EnumTypeUrgence;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SignalementResponseDto {

    private Long idSignalement;

    private String nomCompletCitoyen;

    private String nomCategorie;

    private String nomStructureAssignee;

    private String codeTrackingUnique;

    private EnumTypeUrgence typeUrgence;

    private EnumStatut statut;

    private String photoAvantUrl;

    private String audioUrl;

    private String description;

    private Double latitudeGPS;

    private Double longitudeGPS;

    private String repereVisuel;

    private LocalDateTime dateHeureAlerte;

    private Long citoyenId;

    private Long categorieId;

    private Long structureAssigneeId;

    private Long preuveResolutionId;
}