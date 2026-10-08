package com.alert360.controller;

import com.alert360.controller.dto.SosResponseDto;
import com.alert360.service.serviceInter.SosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sos")
@RequiredArgsConstructor
@Tag(
        name = "SOS",
        description = "Recherche des structures d'urgence situées à proximité d'une position géographique"
)
@SecurityRequirement(name = "bearerAuth")
public class SosController {

    private final SosService sosService;


    // ============================================================
    // OBTENIR LES STRUCTURES D'URGENCE PROCHES
    // ============================================================

    @GetMapping("/proches")
    @Operation(
            summary = "Obtenir les structures d'urgence proches",
            description = "Retourne les structures d'urgence situées à proximité " +
                    "d'une position GPS donnée. Le rayon de recherche est exprimé en kilomètres " +
                    "et vaut 15 km par défaut."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Structures d'urgence récupérées avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Coordonnées GPS ou rayon de recherche invalides"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès interdit"
            )
    })
    public ResponseEntity<List<SosResponseDto>> obtenirStructuresProches(

            @Parameter(
                    description = "Latitude de la position de l'utilisateur",
                    example = "12.6392",
                    required = true
            )
            @RequestParam("latitude") Double latitude,

            @Parameter(
                    description = "Longitude de la position de l'utilisateur",
                    example = "-8.0029",
                    required = true
            )
            @RequestParam("longitude") Double longitude,

            @Parameter(
                    description = "Rayon de recherche en kilomètres. Valeur par défaut : 15 km.",
                    example = "15",
                    required = false
            )
            @RequestParam(
                    value = "rayonKm",
                    required = false,
                    defaultValue = "15"
            ) Double rayonKm
    ) {

        List<SosResponseDto> structures =
                sosService.obtenirStructuresUrgenceProches(
                        latitude,
                        longitude,
                        rayonKm
                );

        return ResponseEntity.ok(structures);
    }
}