package com.alert360.controller;

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
@CrossOrigin(origins = "*")
public class SignalementController {

    private final SignalementService signalementService;


    // ==========================================================
    // CREER UN SIGNALEMENT
    // ==========================================================

    @PostMapping
    public ResponseEntity<SignalementResponseDto> creerSignalement(
            @Valid @RequestBody SignalementRequestDto dto
    ) {

        SignalementResponseDto signalement =
                signalementService.creerSignalement(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(signalement);
    }


    // ==========================================================
    // OBTENIR TOUS LES SIGNALEMENTS
    // ==========================================================

    @GetMapping
    public ResponseEntity<List<SignalementResponseDto>> obtenirTousLesSignalements() {

        List<SignalementResponseDto> signalements =
                signalementService.obtenirTousLesSignalements();

        return ResponseEntity.ok(signalements);
    }


    // ==========================================================
    // OBTENIR UN SIGNALEMENT PAR SON ID
    // ==========================================================

    @GetMapping("/{idSignalement}")
    public ResponseEntity<SignalementResponseDto> obtenirParId(
            @PathVariable Long idSignalement
    ) {

        SignalementResponseDto signalement =
                signalementService.obtenirParId(idSignalement);

        return ResponseEntity.ok(signalement);
    }


    // ==========================================================
    // MODIFIER UN SIGNALEMENT
    // ==========================================================

    @PutMapping("/{idSignalement}")
    public ResponseEntity<SignalementResponseDto> modifierSignalement(
            @PathVariable Long idSignalement,
            @Valid @RequestBody SignalementRequestDto dto
    ) {

        SignalementResponseDto signalement =
                signalementService.modifierSignalement(
                        idSignalement,
                        dto
                );

        return ResponseEntity.ok(signalement);
    }


    // ==========================================================
    // CHANGER LE STATUT
    // ==========================================================

    @PatchMapping("/{idSignalement}/statut")
    public ResponseEntity<SignalementResponseDto> changerStatut(
            @PathVariable Long idSignalement,
            @RequestParam EnumStatut statut
    ) {

        SignalementResponseDto signalement =
                signalementService.changerStatut(
                        idSignalement,
                        statut
                );

        return ResponseEntity.ok(signalement);
    }


    // ==========================================================
    // ASSIGNER UNE STRUCTURE COMPÉTENTE
    // ==========================================================

    @PatchMapping("/{idSignalement}/assigner")
    public ResponseEntity<SignalementResponseDto> assignerStructure(
            @PathVariable Long idSignalement,
            @RequestParam Long idStructure
    ) {

        SignalementResponseDto signalement =
                signalementService.assignerStructure(
                        idSignalement,
                        idStructure
                );

        return ResponseEntity.ok(signalement);
    }


    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UN CITOYEN
    // ==========================================================

    @GetMapping("/citoyen/{idCitoyen}")
    public ResponseEntity<List<SignalementResponseDto>> obtenirParCitoyen(
            @PathVariable Long idCitoyen
    ) {

        List<SignalementResponseDto> signalements =
                signalementService.obtenirParCitoyen(idCitoyen);

        return ResponseEntity.ok(signalements);
    }


    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UNE STRUCTURE
    // ==========================================================

    @GetMapping("/structure/{idStructure}")
    public ResponseEntity<List<SignalementResponseDto>> obtenirParStructure(
            @PathVariable Long idStructure
    ) {

        List<SignalementResponseDto> signalements =
                signalementService.obtenirParStructure(idStructure);

        return ResponseEntity.ok(signalements);
    }


    // ==========================================================
    // OBTENIR LES SIGNALEMENTS PAR STATUT
    // ==========================================================

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<SignalementResponseDto>> obtenirParStatut(
            @PathVariable EnumStatut statut
    ) {

        List<SignalementResponseDto> signalements =
                signalementService.obtenirParStatut(statut);

        return ResponseEntity.ok(signalements);
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