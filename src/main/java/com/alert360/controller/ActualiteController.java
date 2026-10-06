package com.alert360.controller;

import com.alert360.controller.dto.ActualiteRequestDto;
import com.alert360.controller.dto.ActualiteResponseDto;
import com.alert360.service.serviceInter.ActualiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actualites")
@RequiredArgsConstructor
public class ActualiteController {

    private final ActualiteService actualiteService;

    // =========================
    // PUBLIER UNE ACTUALITE
    // =========================
    @PostMapping
    public ResponseEntity<ActualiteResponseDto> publierActualite(
            @Valid @RequestBody ActualiteRequestDto dto
    ) {

        ActualiteResponseDto response =
                actualiteService.publierActualite(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================
    // OBTENIR TOUTES LES ACTUALITES
    // =========================
    @GetMapping
    public ResponseEntity<List<ActualiteResponseDto>> obtenirToutesLesActualites() {

        List<ActualiteResponseDto> actualites =
                actualiteService.obtenirToutesLesActualites();

        return ResponseEntity.ok(actualites);
    }

    // =========================
// OBTENIR UNE ACTUALITE PAR ID
// =========================
    @GetMapping("/{idActualite}")
    public ResponseEntity<ActualiteResponseDto> obtenirActualiteParId(
            @PathVariable Long idActualite
    ) {

        ActualiteResponseDto actualite =
                actualiteService.obtenirActualiteParId(idActualite);

        return ResponseEntity.ok(actualite);
    }

    // =========================
    // MODIFIER UNE ACTUALITE
    // =========================
    @PutMapping("/{idActualite}")
    public ResponseEntity<ActualiteResponseDto> modifierActualite(
            @PathVariable Long idActualite,
            @Valid @RequestBody ActualiteRequestDto dto
    ) {

        ActualiteResponseDto actualite =
                actualiteService.modifierActualite(
                        idActualite,
                        dto
                );

        return ResponseEntity.ok(actualite);
    }

    // =========================
    // SUPPRIMER UNE ACTUALITE
    // =========================
    @DeleteMapping("/{idActualite}")
    public ResponseEntity<Void> supprimerActualite(
            @PathVariable Long idActualite
    ) {

        actualiteService.supprimerActualite(idActualite);

        return ResponseEntity.noContent().build();
    }
}