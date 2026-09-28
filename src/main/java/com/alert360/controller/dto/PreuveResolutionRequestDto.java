package com.alert360.controller.dto;


import com.alert360.entity.enums.EnumTypePreuve;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PreuveResolutionRequestDto {
    @NotNull(message = "Le type de preuve est obligatoire")
    private EnumTypePreuve typePreuve;

    private String photoApresUrl;
    private String rapportTexte;

    @NotNull(message = "L'identifiant du signalement concerné est obligatoire")
    private Long idSignalement;
}
