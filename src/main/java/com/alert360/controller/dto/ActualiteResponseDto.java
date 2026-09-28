package com.alert360.controller.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActualiteResponseDto {

    private Long idActualite;

    private String titre;

    private String corpsTexte;

    private String communeCible;

    private Boolean estUrgent;

    private LocalDateTime datePublication;

    private Long auteurId;
}