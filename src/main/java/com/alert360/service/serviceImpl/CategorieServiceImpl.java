package com.alert360.service;

import com.alert360.controller.dto.CategorieRequestDto;
import com.alert360.controller.dto.CategorieResponseDto;
import com.alert360.entity.Categorie;
import com.alert360.mapper.CategorieRequestMapper;
import com.alert360.mapper.CategorieResponseMapper;
import com.alert360.repository.CategorieRepository;
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

        Categorie categorie =
                categorieRequestMapper.toEntity(dto);

        Categorie categorieEnregistree =
                categorieRepository.save(categorie);

        return categorieResponseMapper.toDto(categorieEnregistree);
    }


    @Override
    public List<CategorieResponseDto> getAllCategories() {

        return categorieRepository.findAll()
                .stream()
                .map(categorieResponseMapper::toDto)
                .toList();
    }


    @Override
    public CategorieResponseDto getCategorieById(Long id) {

        Categorie categorie =
                categorieRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Catégorie introuvable avec l'id : " + id
                                )
                        );

        return categorieResponseMapper.toDto(categorie);
    }


    @Override
    public CategorieResponseDto modifierCategorie(
            Long id,
            CategorieRequestDto dto
    ) {

        Categorie categorie =
                categorieRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Catégorie introuvable avec l'id : " + id
                                )
                        );

        categorie.setNom(dto.getNom());

        Categorie categorieModifiee =
                categorieRepository.save(categorie);

        return categorieResponseMapper.toDto(categorieModifiee);
    }


    @Override
    public void supprimerCategorie(Long id) {

        if (!categorieRepository.existsById(id)) {
            throw new RuntimeException(
                    "Catégorie introuvable avec l'id : " + id
            );
        }

        categorieRepository.deleteById(id);
    }
}