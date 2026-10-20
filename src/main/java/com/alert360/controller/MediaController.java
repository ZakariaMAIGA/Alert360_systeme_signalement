package com.alert360.controller;

import com.alert360.controller.dto.MediaUploadResponseDto;
import com.alert360.service.serviceInter.MediaStorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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



    @GetMapping(
            value = "/photos/{nomFichier:.+}"

    )
    @Operation(summary = "Consulter une photo de signalement")
    public ResponseEntity<Resource> consulterPhoto(
            @PathVariable String nomFichier
    ) {
        Resource photo = mediaStorageService.chargerPhoto(nomFichier);

        String nom = nomFichier.toLowerCase();
        MediaType typeMedia;

        if (nom.endsWith(".png")) {
            typeMedia = MediaType.IMAGE_PNG;
        } else if (nom.endsWith(".webp")) {
            typeMedia = MediaType.parseMediaType("image/webp");
        } else {
            typeMedia = MediaType.IMAGE_JPEG;
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CACHE_CONTROL,
                        "private, max-age=3600"
                )
                .contentType(typeMedia)
                .body(photo);
    }



    @GetMapping("/audios/{nomFichier:.+}")
    @Operation(summary = "Consulter un audio de signalement")
    public ResponseEntity<Resource> consulterAudio(
            @PathVariable String nomFichier
    ) {
        Resource audio = mediaStorageService.chargerAudio(nomFichier);

        String nom = nomFichier.toLowerCase();
        MediaType typeMedia;

        if (nom.endsWith(".m4a")) {
            typeMedia = MediaType.parseMediaType("audio/mp4");
        } else if (nom.endsWith(".mp3")) {
            typeMedia = MediaType.parseMediaType("audio/mpeg");
        } else if (nom.endsWith(".wav")) {
            typeMedia = MediaType.parseMediaType("audio/wav");
        } else if (nom.endsWith(".ogg")) {
            typeMedia = MediaType.parseMediaType("audio/ogg");
        } else {
            typeMedia = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "private, max-age=3600")
                .contentType(typeMedia)
                .body(audio);
    }




    //private String construireUrl(String chemin) {
       // return "/api/medias/" + chemin;
    //}

    private String construireUrl(String chemin) {
        String base = baseUrl.replaceAll("/+$", "");
        String cheminNormalise = chemin.replaceAll("^/+", "");

        if (cheminNormalise.startsWith("api/medias/")) {
            return base + "/" + cheminNormalise;
        }

        return base + "/api/medias/" + cheminNormalise;
    }



    private String nomDepuisChemin(String chemin) {
        return chemin.substring(chemin.lastIndexOf('/') + 1);
    }
}