package com.alert360.controller;

import com.alert360.controller.dto.AbusRequestDto;
import com.alert360.controller.dto.AbusResponseDto;
import com.alert360.entity.enums.EnumStatutAbus;
import com.alert360.service.serviceInter.AbusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/abus")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(
        name = "Signalements d'abus",
        description = "Gestion des signalements d'abus, de leur statut et de leurs pièces jointes"
)
@SecurityRequirement(name = "bearerAuth")
public class AbusController {

    private final AbusService abusService;

    // =========================
    // CLASSER UN SIGNALEMENT COMME ABUS
    // =========================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Signaler un abus",
            description = "Enregistre un signalement d'abus avec les informations fournies et, si nécessaire, une pièce jointe."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Signalement d'abus créé avec succès",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AbusResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Données invalides ou fichier incorrect"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "404", description = "Ressource associée introuvable")
    })
    public ResponseEntity<AbusResponseDto> classerCommeAbus(

            @Parameter(
                    description = "Données du signalement d'abus au format JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AbusRequestDto.class)
                    )
            )
            @Valid
            @RequestPart("abus") AbusRequestDto dto,

            @Parameter(
                    description = "Pièce jointe facultative (image, document ou autre fichier accepté par le backend)",
                    required = false
            )
            @RequestPart(value = "pieceJointe", required = false) MultipartFile pieceJointe
    ) {
        return ResponseEntity.status(201)
                .body(abusService.classerCommeAbus(dto, pieceJointe));
    }

    // =========================
    // OBTENIR TOUS LES SIGNALEMENTS D'ABUS
    // =========================
    @GetMapping
    @Operation(
            summary = "Lister les signalements d'abus",
            description = "Retourne la liste des signalements d'abus enregistrés sur la plateforme."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des signalements d'abus récupérée avec succès",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = AbusResponseDto.class
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès interdit")
    })
    public ResponseEntity<List<AbusResponseDto>> obtenirLesAbus() {
        return ResponseEntity.ok(abusService.obtenirLesAbus());
    }

    // =========================
    // CHANGER LE STATUT D'UN ABUS
    // =========================
    @PatchMapping("/{idAbus}/statut")
    @Operation(
            summary = "Modifier le statut d'un abus",
            description = "Met à jour le statut d'un signalement d'abus à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Statut du signalement d'abus modifié avec succès",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AbusResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Statut invalide"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "404", description = "Signalement d'abus introuvable")
    })
    public ResponseEntity<AbusResponseDto> changerStatut(

            @Parameter(
                    description = "Identifiant du signalement d'abus",
                    example = "1",
                    required = true
            )
            @PathVariable Long idAbus,

            @Parameter(
                    description = "Nouveau statut du signalement d'abus",
                    required = true,
                    schema = @Schema(implementation = EnumStatutAbus.class)
            )
            @RequestParam EnumStatutAbus statut
    ) {
        return ResponseEntity.ok(abusService.changerStatut(idAbus, statut));
    }

    // =========================
    // OBTENIR UNE PIECE JOINTE
    // =========================
    @GetMapping("/{idAbus}/piece-jointe")
    @Operation(
            summary = "Consulter la pièce jointe d'un abus",
            description = "Retourne le fichier associé à un signalement d'abus, si une pièce jointe est disponible."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pièce jointe récupérée avec succès",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
            @ApiResponse(responseCode = "404", description = "Signalement ou pièce jointe introuvable")
    })
    public ResponseEntity<Resource> obtenirPieceJointe(

            @Parameter(
                    description = "Identifiant du signalement d'abus",
                    example = "1",
                    required = true
            )
            @PathVariable Long idAbus
    ) {
        Resource pieceJointe = abusService.obtenirPieceJointe(idAbus);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + pieceJointe.getFilename() + "\""
                )
                .contentType(
                        MediaTypeFactory.getMediaType(pieceJointe)
                                .orElse(MediaType.APPLICATION_OCTET_STREAM)
                )
                .body(pieceJointe);
    }
}