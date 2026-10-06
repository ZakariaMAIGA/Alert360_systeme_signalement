package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumFormat;
import com.alert360.entity.enums.EnumThematique;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContenuEducatifRequestDto {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotNull(message = "La thématique est obligatoire")
    private EnumThematique theme;

    @NotNull(message = "Le format est obligatoire")
    private EnumFormat format;

    @NotBlank(message = "L'URL du média est obligatoire")
    private String mediaUrl;
}