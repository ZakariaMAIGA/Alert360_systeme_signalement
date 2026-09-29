package com.alert360.controller;


import com.alert360.controller.dto.UtilisateurResponseDto;
import com.alert360.service.serviceInter.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/{idUtilisateur}")
    public ResponseEntity<UtilisateurResponseDto> obtenirParId(@PathVariable Long idUtilisateur) {
        UtilisateurResponseDto utilisateur = utilisateurService.obtenirParId(idUtilisateur);
        return ResponseEntity.ok(utilisateur);
    }

    @GetMapping("/email")
    public ResponseEntity<UtilisateurResponseDto> obtenirParEmail(@RequestParam String email) {
        UtilisateurResponseDto utilisateur = utilisateurService.obtenirParEmail(email);
        return ResponseEntity.ok(utilisateur);
    }


    @GetMapping
    public ResponseEntity<List<UtilisateurResponseDto>> obtenirTousLesUtilisateurs() {
        List<UtilisateurResponseDto> utilisateurs = utilisateurService.obtenirTousLesUtilisateurs();
        return ResponseEntity.ok(utilisateurs);
    }

}
