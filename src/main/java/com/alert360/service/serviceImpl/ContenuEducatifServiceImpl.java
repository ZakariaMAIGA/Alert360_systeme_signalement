package com.alert360.service;

import com.alert360.controller.dto.ContenuEducatifRequestDto;
import com.alert360.controller.dto.ContenuEducatifResponseDto;
import com.alert360.entity.Admin;
import com.alert360.entity.ContenuEducatif;
import com.alert360.mapper.ContenuEducatifRequestMapper;
import com.alert360.mapper.ContenuEducatifResponseMapper;
import com.alert360.repository.AdminRepository;
import com.alert360.repository.ContenuEducatifRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContenuEducatifServiceImpl
        implements ContenuEducatifService {

    private final ContenuEducatifRepository contenuEducatifRepository;

    private final AdminRepository adminRepository;

    private final ContenuEducatifRequestMapper contenuEducatifRequestMapper;

    private final ContenuEducatifResponseMapper contenuEducatifResponseMapper;


    @Override
    public ContenuEducatifResponseDto creerContenu(
            ContenuEducatifRequestDto dto
    ) {

        Admin auteur = adminRepository.findById(dto.getAuteurId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Administrateur introuvable avec l'id : "
                                        + dto.getAuteurId()
                        )
                );

        ContenuEducatif contenu =
                contenuEducatifRequestMapper.toEntity(
                        dto,
                        auteur
                );

        ContenuEducatif contenuEnregistre =
                contenuEducatifRepository.save(contenu);

        return contenuEducatifResponseMapper.toDto(
                contenuEnregistre
        );
    }


    @Override
    public List<ContenuEducatifResponseDto> getAllContenus() {

        return contenuEducatifRepository.findAll()
                .stream()
                .map(contenuEducatifResponseMapper::toDto)
                .toList();
    }


    @Override
    public ContenuEducatifResponseDto getContenuById(Long id) {

        ContenuEducatif contenu =
                contenuEducatifRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Contenu éducatif introuvable avec l'id : "
                                                + id
                                )
                        );

        return contenuEducatifResponseMapper.toDto(contenu);
    }


    @Override
    public ContenuEducatifResponseDto modifierContenu(
            Long id,
            ContenuEducatifRequestDto dto
    ) {

        ContenuEducatif contenu =
                contenuEducatifRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Contenu éducatif introuvable avec l'id : "
                                                + id
                                )
                        );

        Admin auteur =
                adminRepository.findById(dto.getAuteurId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Administrateur introuvable avec l'id : "
                                                + dto.getAuteurId()
                                )
                        );

        contenu.setTitre(dto.getTitre());
        contenu.setTheme(dto.getTheme());
        contenu.setFormat(dto.getFormat());
        contenu.setMediaUrl(dto.getMediaUrl());
        contenu.setAuteur(auteur);

        ContenuEducatif contenuModifie =
                contenuEducatifRepository.save(contenu);

        return contenuEducatifResponseMapper.toDto(
                contenuModifie
        );
    }


    @Override
    public void supprimerContenu(Long id) {

        if (!contenuEducatifRepository.existsById(id)) {
            throw new RuntimeException(
                    "Contenu éducatif introuvable avec l'id : " + id
            );
        }

        contenuEducatifRepository.deleteById(id);
    }
}