package com.alert360.controller;

import com.alert360.controller.dto.MediaUploadResponseDto;
import com.alert360.service.serviceInter.MediaStorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/medias")
@RequiredArgsConstructor
@Tag(name = "Médias", description = "Téléversement des photos et fichiers audio")
public class MediaController {

    private final MediaStorageService mediaStorageService;

    @Value("${alert360.media.base-url:http://localhost:8080}")
    private String baseUrl;

    @PostMapping(
            value = "/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Téléverser une photo de signalement")
    public MediaUploadResponseDto televerserPhoto(
            @RequestParam("fichier") MultipartFile fichier
    ) {
        String chemin = mediaStorageService.stockerPhoto(fichier);

        return new MediaUploadResponseDto(
                construireUrl(chemin),
                "PHOTO",
                nomDepuisChemin(chemin)
        );
    }

    @PostMapping(
            value = "/audio",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Téléverser un audio de signalement")
    public MediaUploadResponseDto televerserAudio(
            @RequestParam("fichier") MultipartFile fichier
    ) {
        String chemin = mediaStorageService.stockerAudio(fichier);

        return new MediaUploadResponseDto(
                construireUrl(chemin),
                "AUDIO",
                nomDepuisChemin(chemin)
        );
    }

    private String construireUrl(String chemin) {
        return baseUrl.replaceAll("/+$", "")
                + "/api/medias/" + chemin;
    }

    private String nomDepuisChemin(String chemin) {
        return chemin.substring(chemin.lastIndexOf('/') + 1);
    }
}