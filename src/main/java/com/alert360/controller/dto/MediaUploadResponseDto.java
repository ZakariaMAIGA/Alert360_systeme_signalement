package com.alert360.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MediaUploadResponseDto {

    private String url;
    private String type;
    private String nomFichier;
}