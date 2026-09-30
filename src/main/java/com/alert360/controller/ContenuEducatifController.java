package com.alert360.controller;

import com.alert360.controller.dto.ContenuEducatifRequestDto;
import com.alert360.controller.dto.ContenuEducatifResponseDto;
import com.alert360.service.serviceInter.ContenuEducatifService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contenus")
@RequiredArgsConstructor
public class ContenuEducatifController {

    private  final ContenuEducatifService contenuEducatifService;

    // =========================
    // CREER UN CONTENU EDUCATIF
    // =========================
    @PostMapping
    public ResponseEntity<ContenuEducatifResponseDto> creerContenu(
            @Valid @RequestBody ContenuEducatifRequestDto dto
    ) {

        ContenuEducatifResponseDto response =
                contenuEducatifService.creerContenu(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================
    // OBTENIR TOUS LES CONTENUS
    // =========================
    @GetMapping
    public ResponseEntity<List<ContenuEducatifResponseDto>> obtenirTousLesContenus() {

        List<ContenuEducatifResponseDto> contenus =
                contenuEducatifService.obtenirTousLesContenus();

        return ResponseEntity.ok(contenus);
    }

    // =========================
    // OBTENIR UN CONTENU PAR ID
    // =========================
    @GetMapping("/{idContenu}")
    public ResponseEntity<ContenuEducatifResponseDto> obtenirContenuParId(
            @PathVariable Long idContenu
    ) {

        ContenuEducatifResponseDto contenu =
                contenuEducatifService.obetnirParId(idContenu);

        return ResponseEntity.ok(contenu);
    }

    // =========================
    // MODIFIER UN CONTENU
    // =========================
    @PutMapping("/{idContenu}")
    public ResponseEntity<ContenuEducatifResponseDto> modifierContenu(
            @PathVariable Long idContenu,
            @Valid @RequestBody ContenuEducatifRequestDto dto
    ) {

        ContenuEducatifResponseDto contenu =
                contenuEducatifService.modifierContenu(
                        idContenu,
                        dto
                );

        return ResponseEntity.ok(contenu);
    }

    // =========================
    // SUPPRIMER UN CONTENU
    // =========================
    @DeleteMapping("/{idContenu}")
    public ResponseEntity<Void> supprimerContenu(
            @PathVariable Long idContenu
    ) {

        contenuEducatifService.supprimerContenu(idContenu);

        return ResponseEntity.noContent().build();
    }
}