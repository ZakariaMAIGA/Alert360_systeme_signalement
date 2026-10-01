package com.alert360.controller.dto;

import com.alert360.entity.enums.EnumFormat;
import com.alert360.entity.enums.EnumThematique;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContenuEducatifResponseDto {

    private Long idContenu;

    private String titre;

    private EnumThematique theme;

    private EnumFormat format;

    private String mediaUrl;

    private LocalDateTime datePublication;

    private Long auteurId;

    private String nomAuteur;
}