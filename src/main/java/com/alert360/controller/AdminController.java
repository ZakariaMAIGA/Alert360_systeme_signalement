package com.alert360.controller;

import com.alert360.controller.dto.UtilisateurResponseDto;
import com.alert360.service.serviceInter.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/utilisateurs/{idUtilisateur}")
    public ResponseEntity<UtilisateurResponseDto> obtenirUtilisateurParId(@PathVariable Long idUtilisateur) {
        UtilisateurResponseDto utilisateur = adminService.obtenirUtilisateurParId(idUtilisateur);
        return ResponseEntity.ok(utilisateur);
    }

    @GetMapping("/utilisateurs")
    public ResponseEntity<List<UtilisateurResponseDto>> obtenirTousLesUtilisateurs() {
        List<UtilisateurResponseDto> utilisateurs = adminService.obtenirTousLesUtilisateurs();
        return ResponseEntity.ok(utilisateurs);
    }

    @PatchMapping("/utilisateurs/{idUtilisateur}/statut")
    public ResponseEntity<UtilisateurResponseDto> changerStatutCompte(
            @PathVariable Long idUtilisateur,
            @RequestParam Boolean estActif) {
        UtilisateurResponseDto utilisateurAjourne = adminService.changerStatutCompte(idUtilisateur, estActif);
        return ResponseEntity.ok(utilisateurAjourne);
    }
}
