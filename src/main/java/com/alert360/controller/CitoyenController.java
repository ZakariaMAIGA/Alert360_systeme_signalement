package com.alert360.controller;

import com.alert360.controller.dto.APIResponse;
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
    public ResponseEntity<APIResponse<Void>> creerCitoyen(
            @Valid @RequestBody CitoyenRequestDto dto) {
        CitoyenResponseDto nouveauCitoyen = citoyenService.creerCitoyen(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Citoyen créé avec succès.",
                        null));
    }

    @PutMapping("/{idUtilisateur}")
    public ResponseEntity<APIResponse<Object>> modifierCitoyen(
            @PathVariable Long idUtilisateur,
            @Valid @RequestBody CitoyenRequestDto dto) {
        CitoyenResponseDto citoyenModifie = citoyenService.modifierCitoyen(idUtilisateur, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Signalement modifié avec succès.",
                        citoyenModifie));
    }

    @GetMapping("/{idUtilisateur}")
    public ResponseEntity<APIResponse<CitoyenResponseDto>> obtenirParId(@PathVariable Long idUtilisateur) {
        CitoyenResponseDto citoyen = citoyenService.obtenirParId(idUtilisateur);
        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Signalement trouvé avec succès.",
                        citoyen));
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<CitoyenResponseDto>>> obtenirTousLesCitoyens() {
        List<CitoyenResponseDto> citoyens = citoyenService.obtenirTousLesCitoyens();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(
                                true,
                                "Les citoyens sont trouvés avec succès",
                                citoyens
                        )
                );
    }

    @GetMapping("/quartier/{quartier}")
    public ResponseEntity<APIResponse<List<CitoyenResponseDto>>> obtenirParQuartier(@PathVariable String quartier) {
        List<CitoyenResponseDto> citoyens = citoyenService.obtenirParQuartier(quartier);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(
                                true,
                                "Les citoyens par quartiers sont trouvés avec succès",
                                citoyens
                        )
                );
    }

    @PatchMapping("/{idUtilisateur}/badges")
    public ResponseEntity<APIResponse<CitoyenResponseDto>>ajouterBadgeCivique(
            @PathVariable Long idUtilisateur,
            @RequestParam String badge) {
        CitoyenResponseDto citoyenAjourne = citoyenService.ajouterBadgeCivique(idUtilisateur, badge);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(
                                true,
                                "Le citoyen par badge sont trouvés avec succès",
                                citoyenAjourne
                        )
                );
    }

    @DeleteMapping("/{idUtilisateur}")
    public ResponseEntity<APIResponse<Void>> supprimerCitoyen(@PathVariable Long idUtilisateur) {
        citoyenService.supprimerCitoyen(idUtilisateur);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(
                                true,
                                "Le citoyen est supprimé",
                                null

                        )
                );
    }
}
