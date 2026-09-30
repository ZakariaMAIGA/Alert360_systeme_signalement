package com.alert360.controller;

import com.alert360.controller.dto.StructureCompetenteRequestDto;
import com.alert360.controller.dto.StructureCompetenteResponseDto;

import com.alert360.entity.enums.EnumTypeStructure;
import com.alert360.service.serviceInter.StructureCompetenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/structures")
@RequiredArgsConstructor
public class StructureCompetenteController {

    private final StructureCompetenteService structureCompetenteService;

    // =========================
    // CREER UNE STRUCTURE
    // =========================
    @PostMapping
    public ResponseEntity<StructureCompetenteResponseDto> creerStructure(
            @Valid @RequestBody StructureCompetenteRequestDto dto
    ) {

        StructureCompetenteResponseDto response =
                structureCompetenteService.creerStructure(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================
    // OBTENIR TOUTES LES STRUCTURES
    // =========================
    @GetMapping
    public ResponseEntity<List<StructureCompetenteResponseDto>> obtenirToutesLesStructures() {

        List<StructureCompetenteResponseDto> structures =
                structureCompetenteService.obtenirToutesLesStructures();

        return ResponseEntity.ok(structures);
    }

    // =========================
    // OBTENIR UNE STRUCTURE PAR ID
    // =========================
    @GetMapping("/{idStructure}")
    public ResponseEntity<StructureCompetenteResponseDto> obtenirStructureParId(
            @PathVariable Long idStructure
    ) {

        StructureCompetenteResponseDto structure =
                structureCompetenteService.obtenirParId(idStructure);

        return ResponseEntity.ok(structure);
    }

    // =========================
    // OBTENIR LES STRUCTURES PAR TYPE
    // =========================
    @GetMapping("/type/{typeStructure}")
    public ResponseEntity<List<StructureCompetenteResponseDto>> obtenirParType(
            @PathVariable EnumTypeStructure typeStructure
    ) {

        List<StructureCompetenteResponseDto> structures =
                structureCompetenteService.obtenirPartype(typeStructure);

        return ResponseEntity.ok(structures);
    }

    // =========================
    // MODIFIER UNE STRUCTURE
    // =========================
    @PutMapping("/{idStructure}")
    public ResponseEntity<StructureCompetenteResponseDto> modifierStructure(
            @PathVariable Long idStructure,
            @Valid @RequestBody StructureCompetenteRequestDto dto
    ) {

        StructureCompetenteResponseDto structure =
                structureCompetenteService.modifierStructure(
                        idStructure,
                        dto
                );

        return ResponseEntity.ok(structure);
    }

    // =========================
    // SUPPRIMER UNE STRUCTURE
    // =========================
    @DeleteMapping("/{idStructure}")
    public ResponseEntity<Void> supprimerStructure(
            @PathVariable Long idStructure
    ) {

        structureCompetenteService.supprimerStructre(idStructure);

        return ResponseEntity.noContent().build();
    }
}