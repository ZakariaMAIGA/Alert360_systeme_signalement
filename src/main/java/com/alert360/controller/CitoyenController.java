package com.alert360.controller;

import com.alert360.controller.dto.CitoyenRequestDto;
import com.alert360.controller.dto.CitoyenResponseDto;
import com.alert360.service.serviceInter.CitoyenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citoyens")
@RequiredArgsConstructor
public class CitoyenController {
    private final CitoyenService citoyenService;

    @PostMapping
    public ResponseEntity<CitoyenResponseDto> creerCitoyen(
            @Valid @RequestBody CitoyenRequestDto dto) {
        CitoyenResponseDto nouveauCitoyen = citoyenService.creerCitoyen(dto);
        return new ResponseEntity<>(nouveauCitoyen, HttpStatus.CREATED);
    }

    @PutMapping("/{idUtilisateur}")
    public ResponseEntity<CitoyenResponseDto> modifierCitoyen(
            @PathVariable Long idUtilisateur,
            @Valid @RequestBody CitoyenRequestDto dto) {
        CitoyenResponseDto citoyenModifie = citoyenService.modifierCitoyen(idUtilisateur, dto);
        return ResponseEntity.ok(citoyenModifie);
    }

    @GetMapping("/{idUtilisateur}")
    public ResponseEntity<CitoyenResponseDto> obtenirParId(@PathVariable Long idUtilisateur) {
        CitoyenResponseDto citoyen = citoyenService.obtenirParId(idUtilisateur);
        return ResponseEntity.ok(citoyen);
    }

    @GetMapping
    public ResponseEntity<List<CitoyenResponseDto>> obtenirTousLesCitoyens() {
        List<CitoyenResponseDto> citoyens = citoyenService.obtenirTousLesCitoyens();
        return ResponseEntity.ok(citoyens);
    }

    @GetMapping("/quartier/{quartier}")
    public ResponseEntity<List<CitoyenResponseDto>> obtenirParQuartier(@PathVariable String quartier) {
        List<CitoyenResponseDto> citoyens = citoyenService.obtenirParQuartier(quartier);
        return ResponseEntity.ok(citoyens);
    }

    @PatchMapping("/{idUtilisateur}/badges")
    public ResponseEntity<CitoyenResponseDto> ajouterBadgeCivique(
            @PathVariable Long idUtilisateur,
            @RequestParam String badge) {
        CitoyenResponseDto citoyenAjourne = citoyenService.ajouterBadgeCivique(idUtilisateur, badge);
        return ResponseEntity.ok(citoyenAjourne);
    }

    @DeleteMapping("/{idUtilisateur}")
    public ResponseEntity<Void> supprimerCitoyen(@PathVariable Long idUtilisateur) {
        citoyenService.supprimerCitoyen(idUtilisateur);
        return ResponseEntity.noContent().build();
    }
}
