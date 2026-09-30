package com.alert360.controller;

import com.alert360.controller.dto.APIResponse;
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


    // CREER UN SIGNALEMENT

    @PostMapping
    public ResponseEntity<APIResponse<Void>> creerSignalement(
            @Valid @RequestBody SignalementRequestDto dto
    ) {

        SignalementResponseDto signalement =
                signalementService.creerSignalement(dto);

         return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Signalement créé avec succès.",
                        null));
    }



    // OBTENIR TOUS LES SIGNALEMENTS


    @GetMapping
    public ResponseEntity<APIResponse<List<SignalementResponseDto>>> obtenirTousLesSignalements() {

        List<SignalementResponseDto> signalements =
                signalementService.obtenirTousLesSignalements();

        return ResponseEntity.ok(
                new APIResponse<>(
                        true,
                        "Liste des siganlements récupérée avec succès.",
                        signalements));

    }



    // OBTENIR UN SIGNALEMENT PAR SON ID

    @GetMapping("/{idSignalement}")
    public ResponseEntity<APIResponse<SignalementResponseDto>> obtenirParId(
            @PathVariable Long idSignalement
    ) {

        SignalementResponseDto signalement =
                signalementService.obtenirParId(idSignalement);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(
                                true,
                                "Signalement trouve avec success",
                                signalement
                        )
                );

    }



    // MODIFIER UN SIGNALEMENT


    @PutMapping("/{idSignalement}")
    public ResponseEntity<APIResponse<SignalementResponseDto>> modifierSignalement(
            @PathVariable Long idSignalement,
            @Valid @RequestBody SignalementRequestDto dto
    ) {

        SignalementResponseDto signalement =
                signalementService.modifierSignalement(
                        idSignalement,
                        dto
                );

        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(
                                true,
                                "Signalement modifié avec success",
                                signalement
                        )
                );

    }


    // CHANGER LE STATUT


    @PatchMapping("/{idSignalement}/statut")
    public ResponseEntity<APIResponse<SignalementResponseDto>> changerStatut(
            @PathVariable Long idSignalement,
            @RequestParam EnumStatut statut
    ) {

        SignalementResponseDto signalement =
                signalementService.changerStatut(
                        idSignalement,
                        statut
                );

        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(
                                true,
                                "Le statut du signalement est changé trouve avec success",
                                signalement
                        )
                );
    }



    // ASSIGNER UNE STRUCTURE COMPÉTENTE

    @PatchMapping("/{idSignalement}/assigner")
    public ResponseEntity<APIResponse<SignalementResponseDto>> assignerStructure(
            @PathVariable Long idSignalement,
            @RequestParam Long idStructure
    ) {

        SignalementResponseDto signalement =
                signalementService.assignerStructure(
                        idSignalement,
                        idStructure
                );

        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(
                                true,
                                "Le signalement a été assigné avec success",
                                null
                        )
                );
    }


    // OBTENIR LES SIGNALEMENTS D'UN CITOYEN

    @GetMapping("/citoyen/{idCitoyen}")
    public ResponseEntity<APIResponse<List<SignalementResponseDto>>> obtenirParCitoyen(
            @PathVariable Long idCitoyen
    ) {

        List<SignalementResponseDto> signalements =
                signalementService.obtenirParCitoyen(idCitoyen);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(
                                true,
                                "Les signalement sont trouvés avec succès",
                                signalements
                        )
                );
    }



    // OBTENIR LES SIGNALEMENTS D'UNE STRUCTURE


    @GetMapping("/structure/{idStructure}")
    public ResponseEntity<APIResponse<List<SignalementResponseDto>>> obtenirParStructure(
            @PathVariable Long idStructure
    ) {

        List<SignalementResponseDto> signalements =
                signalementService.obtenirParStructure(idStructure);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(
                                true,
                                "Les signalement sont trouvés avec succès",
                                signalements
                        )
                );
    }



    // OBTENIR LES SIGNALEMENTS PAR STATUT


    @GetMapping("/statut/{statut}")
    public ResponseEntity<APIResponse<List<SignalementResponseDto>>> obtenirParStatut(
            @PathVariable EnumStatut statut
    ) {

        List<SignalementResponseDto> signalements =
                signalementService.obtenirParStatut(statut);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(
                                true,
                                "Les signalement par statuts sont trouvés avec succès",
                                signalements
                        )
                );
    }



    // SUPPRIMER UN SIGNALEMENT


    @DeleteMapping("/{idSignalement}")
    public ResponseEntity<APIResponse<Void>> supprimerSignalement(
            @PathVariable Long idSignalement
    ) {

        signalementService.supprimerSignalement(idSignalement);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new APIResponse<>(
                                true,
                                "Le signalement a été supprimé avec success",
                                null
                        )
                );
    }
}