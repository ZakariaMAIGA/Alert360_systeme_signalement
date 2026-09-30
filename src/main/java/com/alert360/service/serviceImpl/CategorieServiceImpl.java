package com.alert360.service.serviceImpl;
import com.alert360.controller.dto.CategorieRequestDto;
import com.alert360.controller.dto.CategorieResponseDto;
import com.alert360.entity.Categorie;
import com.alert360.mapper.Request.CategorieRequestMapper;
import com.alert360.mapper.Response.CategorieResponseMapper;
import com.alert360.repository.CategorieRepository;
import com.alert360.service.serviceInter.CategorieService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategorieServiceImpl implements CategorieService {

    private final CategorieRepository categorieRepository;

    private final CategorieRequestMapper categorieRequestMapper;

    private final CategorieResponseMapper categorieResponseMapper;



    @Override
    public CategorieResponseDto creerCategorie(
            CategorieRequestDto dto
    ) {

        // 1. Transformer le DTO en Entity
        Categorie categorie =
                categorieRequestMapper.toEntity(dto);

        // 2. Enregistrer dans la base de données
        Categorie categorieEnregistree =
                categorieRepository.save(categorie);

        // 3. Transformer l'Entity en Response DTO
        return categorieResponseMapper.toDto(
                categorieEnregistree
        );
    }



    @Override
    public CategorieResponseDto modifierCategorie(
            Long idCategorie,
            CategorieRequestDto dto
    ) {

        // 1. Chercher la catégorie
        Categorie categorie =
                categorieRepository.findById(idCategorie)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Catégorie introuvable avec l'id : "
                                                + idCategorie
                                )
                        );

        // 2. Modifier les informations
        categorie.setNom(dto.getNom());

        // 3. Enregistrer les modifications
        Categorie categorieModifiee =
                categorieRepository.save(categorie);

        // 4. Retourner le DTO
        return categorieResponseMapper.toDto(
                categorieModifiee
        );
    }



    @Override
    public CategorieResponseDto obtenirParId(
            Long idCategorie
    ) {

        // 1. Chercher la catégorie
        Categorie categorie =
                categorieRepository.findById(idCategorie)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Catégorie introuvable avec l'id : "
                                                + idCategorie
                                )
                        );

        // 2. Transformer Entity -> DTO
        return categorieResponseMapper.toDto(categorie);
    }



    // OBTENIR TOUTES LES CATEGORIES

    @Override
    public List<CategorieResponseDto> obtenirToutesLesCategories() {

        return categorieRepository.findAll()
                .stream()
                .map(categorieResponseMapper::toDto)
                .toList();
    }



    // SUPPRIMER UNE CATEGORIE
    @Override
    public void supprimerCategorie(
            Long idCategorie
    ) {

        // Vérifier que la catégorie existe
        if (!categorieRepository.existsById(idCategorie)) {

            throw new EntityNotFoundException(
                    "Catégorie introuvable avec l'id : "
                            + idCategorie
            );
        }

        // Supprimer
        categorieRepository.deleteById(idCategorie);
    }
}