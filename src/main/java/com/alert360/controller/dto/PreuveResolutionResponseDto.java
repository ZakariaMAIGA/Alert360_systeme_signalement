package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumTypePreuve;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PreuveResolutionResponseDto {
    private Long idPreuve;
    private EnumTypePreuve typePreuve;
    private String photoApresUrl;
    private String rapportTexte;
    private LocalDateTime dateResolution;
    private Long idSignalement;
    private String nomAgent;
    private String prenomAgent;
    private String telephoneAgent;
    private String matriculeAgent;
}
