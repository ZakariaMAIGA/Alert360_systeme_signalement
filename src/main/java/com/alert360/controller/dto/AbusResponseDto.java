package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumNiveauGraviteAbus;
import com.alert360.entity.enums.EnumStatutAbus;
import com.alert360.entity.enums.EnumTypeAbus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AbusResponseDto {
    private Long idAbus;
    private Long idSignalement;
    private String codeTrackingUnique;
    private String citoyenNomComplet;
    private String categorie;
    private EnumTypeAbus typeAbus;
    private String typeAbusPersonnalise;
    private EnumNiveauGraviteAbus niveauGravite;
    private EnumStatutAbus statut;
    private String justification;
    private String pieceJointeUrl;
    private LocalDateTime dateClassement;
    private String nomStructure;
}
