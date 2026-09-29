package com.alert360.controller;


import com.alert360.controller.dto.ActionCitoyenneRequestDto;
import com.alert360.controller.dto.ActionCitoyenneResponseDto;
import com.alert360.service.serviceInter.ActionCitoyenneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
public class ActionCitoyenneController {
    private final ActionCitoyenneService actionCitoyenneService;

    @PostMapping
    public ResponseEntity<ActionCitoyenneResponseDto> creerAction(
            @Valid @RequestBody ActionCitoyenneRequestDto dto) {
        ActionCitoyenneResponseDto nouvelleAction = actionCitoyenneService.creerAction(dto);
        return new ResponseEntity<>(nouvelleAction, HttpStatus.CREATED);
    }

    @PutMapping("/{idAction}")
    public ResponseEntity<ActionCitoyenneResponseDto> modifierAction(
            @PathVariable Long idAction,
            @Valid @RequestBody ActionCitoyenneRequestDto dto) {
        ActionCitoyenneResponseDto actionModifiee = actionCitoyenneService.modifierAction(idAction, dto);
        return ResponseEntity.ok(actionModifiee);
    }

    @GetMapping("/{idAction}")
    public ResponseEntity<ActionCitoyenneResponseDto> obtenirParId(@PathVariable Long idAction) {
        ActionCitoyenneResponseDto action = actionCitoyenneService.obtenirParId(idAction);
        return ResponseEntity.ok(action);
    }

    @GetMapping
    public ResponseEntity<List<ActionCitoyenneResponseDto>> obtenirToutesLesActions() {
        List<ActionCitoyenneResponseDto> actions = actionCitoyenneService.obtenirToutesLesActions();
        return ResponseEntity.ok(actions);
    }

    @DeleteMapping("/{idAction}")
    public ResponseEntity<Void> supprimerAction(@PathVariable Long idAction) {
        actionCitoyenneService.supprimerAction(idAction);
        return ResponseEntity.noContent().build();
    }
}
