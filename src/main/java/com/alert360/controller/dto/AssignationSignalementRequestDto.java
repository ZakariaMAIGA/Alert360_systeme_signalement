package com.alert360.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignationSignalementRequestDto {

    @NotNull(message = "L'ID de l'agent destinataire est obligatoire")
    private Long idAgent;
}