package com.alert360.controller;

import com.alert360.controller.dto.APIResponse;
import com.alert360.controller.dto.CategorieRequestDto;
import com.alert360.controller.dto.CategorieResponseDto;
import com.alert360.service.serviceInter.CategorieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategorieController {

   private final CategorieService categorieService;


    // CREER UNE CATEGORIE

    @PostMapping
    public ResponseEntity<APIResponse<Void>> creerCategorie(
            @Valid @RequestBody CategorieRequestDto dto
    ) {

        CategorieResponseDto response =
                categorieService.creerCategorie(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse<>(
                        true,
                        "Categorie créé avec succès.",
                        null));
    }


    // OBTENIR TOUTES LES CATEGORIES

    @GetMapping
    public ResponseEntity<List<CategorieResponseDto>> obtenirToutesLesCategories() {

        List<CategorieResponseDto> categories =
                categorieService.obtenirToutesLesCategories();

        return ResponseEntity.ok(categories);
    }

    // =========================
    // OBTENIR UNE CATEGORIE PAR ID
    // =========================
    @GetMapping("/{idCategorie}")
    public ResponseEntity<CategorieResponseDto> obtenirCategorieParId(
            @PathVariable Long idCategorie
    ) {

        CategorieResponseDto categorie =
                categorieService.obtenirParId(idCategorie);

        return ResponseEntity.ok(categorie);
    }

    // =========================
    // MODIFIER UNE CATEGORIE
    // =========================
    @PutMapping("/{idCategorie}")
    public ResponseEntity<CategorieResponseDto> modifierCategorie(
            @PathVariable Long idCategorie,
            @Valid @RequestBody CategorieRequestDto dto
    ) {

        CategorieResponseDto categorie =
                categorieService.modifierCategorie(idCategorie, dto);

        return ResponseEntity.ok(categorie);
    }

    // =========================
    // SUPPRIMER UNE CATEGORIE
    // =========================
    @DeleteMapping("/{idCategorie}")
    public ResponseEntity<Void> supprimerCategorie(
            @PathVariable Long idCategorie
    ) {

        categorieService.supprimerCategorie(idCategorie);

        return ResponseEntity.noContent().build();
    }
}