package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumStatutAction;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActionCitoyenneResponseDto {
    private Long idAction;
    private LocalDateTime dateHeureRendezVous;
    private String lieuRassemblement;
    private Integer nombreParticipantsInscrits;
    private EnumStatutAction statutAction;
    private Long idSignalementOrigine;
}

