package com.alert360.controller;

import com.alert360.controller.dto.AdminUtilisateurCreateDto;
import com.alert360.controller.dto.AdminUtilisateurUpdateDto;
import com.alert360.controller.dto.UtilisateurResponseDto;
import com.alert360.service.serviceInter.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/utilisateurs/{idUtilisateur}")
    public ResponseEntity<UtilisateurResponseDto> obtenirUtilisateurParId(
            @PathVariable Long idUtilisateur) {

        return ResponseEntity.ok(
                adminService.obtenirUtilisateurParId(idUtilisateur)
        );
    }

    @GetMapping("/utilisateurs")
    public ResponseEntity<List<UtilisateurResponseDto>>
    obtenirTousLesUtilisateurs() {

        return ResponseEntity.ok(
                adminService.obtenirTousLesUtilisateurs()
        );
    }

    @PostMapping("/utilisateurs")
    public ResponseEntity<UtilisateurResponseDto> creerUtilisateur(
            @Valid @RequestBody AdminUtilisateurCreateDto dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(adminService.creerUtilisateur(dto));
    }

    @PutMapping("/utilisateurs/{idUtilisateur}")
    public ResponseEntity<UtilisateurResponseDto> modifierUtilisateur(
            @PathVariable Long idUtilisateur,
            @Valid @RequestBody AdminUtilisateurUpdateDto dto) {

        return ResponseEntity.ok(
                adminService.modifierUtilisateur(
                        idUtilisateur,
                        dto
                )
        );
    }

    @PatchMapping("/utilisateurs/{idUtilisateur}/statut")
    public ResponseEntity<UtilisateurResponseDto> changerStatutCompte(
            @PathVariable Long idUtilisateur,
            @RequestParam Boolean estActif) {

        return ResponseEntity.ok(
                adminService.changerStatutCompte(
                        idUtilisateur,
                        estActif
                )
        );
    }
}