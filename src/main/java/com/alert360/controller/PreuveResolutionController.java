package com.alert360.controller;

import com.alert360.controller.dto.PreuveResolutionRequestDto;
import com.alert360.controller.dto.PreuveResolutionResponseDto;
import com.alert360.service.serviceInter.PreuveResolutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preuves")
@RequiredArgsConstructor
public class PreuveResolutionController {

    private final PreuveResolutionService preuveResolutionService;

    @PostMapping
    public ResponseEntity<PreuveResolutionResponseDto> ajouterPreuveResolution(
            @Valid @RequestBody PreuveResolutionRequestDto dto) {
        PreuveResolutionResponseDto preuve = preuveResolutionService.ajouterPreuveResolution(dto);
        return new ResponseEntity<>(preuve, HttpStatus.CREATED);
    }

    @PutMapping("/{idPreuve}")
    public ResponseEntity<PreuveResolutionResponseDto> modifierPreuveResolution(
            @PathVariable Long idPreuve,
            @Valid @RequestBody PreuveResolutionRequestDto dto) {
        PreuveResolutionResponseDto preuveModifiee = preuveResolutionService.modifierPreuveResolution(idPreuve, dto);
        return ResponseEntity.ok(preuveModifiee);
    }

    @GetMapping("/{idPreuve}")
    public ResponseEntity<PreuveResolutionResponseDto> obtenirParId(@PathVariable Long idPreuve) {
        PreuveResolutionResponseDto preuve = preuveResolutionService.obtenirParId(idPreuve);
        return ResponseEntity.ok(preuve);
    }

    @GetMapping("/signalement/{idSignalement}")
    public ResponseEntity<PreuveResolutionResponseDto> obtenirParSignalement(@PathVariable Long idSignalement) {
        PreuveResolutionResponseDto preuve = preuveResolutionService.obtenirParSignalement(idSignalement);
        return ResponseEntity.ok(preuve);
    }

    @GetMapping
    public ResponseEntity<List<PreuveResolutionResponseDto>> obtenirToutesLesPreuves() {
        List<PreuveResolutionResponseDto> preuves = preuveResolutionService.obtenirToutesLesPreuves();
        return ResponseEntity.ok(preuves);
    }

    @DeleteMapping("/{idPreuve}")
    public ResponseEntity<Void> supprimerPreuve(@PathVariable Long idPreuve) {
        preuveResolutionService.supprimerPreuve(idPreuve);
        return ResponseEntity.noContent().build();
    }

}
