package com.alert360.controller.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActionCitoyenneRequestDto {

    @NotNull(message = "La date et l'heure de rendez-vous sont obligatoires")
    @Future(message = "La date de rendez-vous doit être dans le futur")
    private LocalDateTime dateHeureRendezVous;

    @NotBlank(message = "Le lieu de rassemblement est obligatoire")
    private String lieuRassemblement;

    private Long idSignalementOrigine;
}
