package com.alert360.controller;

import com.alert360.controller.dto.AssignationSignalementRequestDto;
import com.alert360.controller.dto.SignalementRequestDto;
import com.alert360.controller.dto.SignalementResponseDto;
import com.alert360.entity.enums.EnumStatut;
import com.alert360.service.serviceInter.SignalementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/signalements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE})
public class SignalementController {

    private final SignalementService signalementService;

    // ==========================================================
    // CREER UN SIGNALEMENT (Routage automatique PostGIS)
    // ==========================================================

    @PostMapping
    public ResponseEntity<SignalementResponseDto> creerSignalement(
            @Valid @RequestBody SignalementRequestDto dto
    ) {
        SignalementResponseDto signalement = signalementService.creerSignalement(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(signalement);
    }

    // ==========================================================
    // OBTENIR TOUS LES SIGNALEMENTS
    // ==========================================================

    @GetMapping
    public ResponseEntity<List<SignalementResponseDto>> obtenirTousLesSignalements() {
        return ResponseEntity.ok(signalementService.obtenirTousLesSignalements());
    }

    // ==========================================================
    // OBTENIR UN SIGNALEMENT PAR SON ID
    // ==========================================================

    @GetMapping("/{idSignalement}")
    public ResponseEntity<SignalementResponseDto> obtenirParId(
            @PathVariable Long idSignalement
    ) {
        return ResponseEntity.ok(signalementService.obtenirParId(idSignalement));
    }

    // ==========================================================
    // MODIFIER UN SIGNALEMENT (Recalcule la structure si GPS modifié)
    // ==========================================================

    @PutMapping("/{idSignalement}")
    public ResponseEntity<SignalementResponseDto> modifierSignalement(
            @PathVariable Long idSignalement,
            @Valid @RequestBody SignalementRequestDto dto
    ) {
        return ResponseEntity.ok(signalementService.modifierSignalement(idSignalement, dto));
    }

    // ==========================================================
    // CHANGER LE STATUT
    // ==========================================================

    @PatchMapping("/{idSignalement}/statut")
    public ResponseEntity<SignalementResponseDto> changerStatut(
            @PathVariable Long idSignalement,
            @RequestParam EnumStatut statut
    ) {
        return ResponseEntity.ok(signalementService.changerStatut(idSignalement, statut));
    }

    // ==========================================================
    // ASSIGNER / RÉASSIGNER MANUELLEMENT UNE STRUCTURE COMPÉTENTE
    // ==========================================================

    @PatchMapping("/{idSignalement}/assigner")
    public ResponseEntity<SignalementResponseDto> assignerStructure(
            @PathVariable Long idSignalement,
            @RequestParam Long idStructure
    ) {
        return ResponseEntity.ok(signalementService.assignerStructure(idSignalement, idStructure));
    }

    // ==========================================================
    // ASSIGNER UN AGENT DE TERRAIN (RÉSERVÉ AU RESPONSABLE)
    // ==========================================================

    @PatchMapping("/{idSignalement}/assigner-agent")
    public ResponseEntity<SignalementResponseDto> assignerAgent(
            @PathVariable Long idSignalement,
            @Valid @RequestBody AssignationSignalementRequestDto dto
    ) {
        return ResponseEntity.ok(signalementService.assignerAgent(idSignalement, dto));
    }

    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UN CITOYEN
    // ==========================================================

    @GetMapping("/citoyen/{idCitoyen}")
    public ResponseEntity<List<SignalementResponseDto>> obtenirParCitoyen(
            @PathVariable Long idCitoyen
    ) {
        return ResponseEntity.ok(signalementService.obtenirParCitoyen(idCitoyen));
    }

    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UNE STRUCTURE
    // ==========================================================

    @GetMapping("/structure/{idStructure}")
    public ResponseEntity<List<SignalementResponseDto>> obtenirParStructure(
            @PathVariable Long idStructure
    ) {
        return ResponseEntity.ok(signalementService.obtenirParStructure(idStructure));
    }

    // ==========================================================
    // OBTENIR LES SIGNALEMENTS ASSIGNÉS À UN AGENT TERRAIN
    // ==========================================================

    @GetMapping("/agent/{idAgent}")
    public ResponseEntity<List<SignalementResponseDto>> obtenirParAgentAssigne(
            @PathVariable Long idAgent
    ) {
        return ResponseEntity.ok(signalementService.obtenirParAgentAssigne(idAgent));
    }

    // ==========================================================
    // OBTENIR LES SIGNALEMENTS PAR STATUT
    // ==========================================================

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<SignalementResponseDto>> obtenirParStatut(
            @PathVariable EnumStatut statut
    ) {
        return ResponseEntity.ok(signalementService.obtenirParStatut(statut));
    }

    // ==========================================================
    // SUPPRIMER UN SIGNALEMENT
    // ==========================================================

    @DeleteMapping("/{idSignalement}")
    public ResponseEntity<Void> supprimerSignalement(
            @PathVariable Long idSignalement
    ) {
        signalementService.supprimerSignalement(idSignalement);
        return ResponseEntity.noContent().build();
    }
}