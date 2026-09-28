package com.alert360.service;

import com.alert360.controller.dto.ActualiteRequestDto;
import com.alert360.controller.dto.ActualiteResponseDto;
import com.alert360.entity.Actualite;
import com.alert360.entity.Admin;
import com.alert360.mapper.ActualiteRequestMapper;
import com.alert360.mapper.ActualiteResponseMapper;
import com.alert360.repository.ActualiteRepository;
import com.alert360.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActualiteServiceImpl implements ActualiteService {

    private final ActualiteRepository actualiteRepository;

    private final AdminRepository adminRepository;

    private final ActualiteRequestMapper actualiteRequestMapper;

    private final ActualiteResponseMapper actualiteResponseMapper;


    @Override
    public ActualiteResponseDto creerActualite(
            ActualiteRequestDto dto
    ) {

        Admin auteur = adminRepository.findById(dto.getAuteurId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Administrateur introuvable avec l'id : "
                                        + dto.getAuteurId()
                        )
                );

        Actualite actualite =
                actualiteRequestMapper.toEntity(
                        dto,
                        auteur
                );

        Actualite actualiteEnregistree =
                actualiteRepository.save(actualite);

        return actualiteResponseMapper.toDto(
                actualiteEnregistree
        );
    }


    @Override
    public List<ActualiteResponseDto> getAllActualites() {

        return actualiteRepository.findAll()
                .stream()
                .map(actualiteResponseMapper::toDto)
                .toList();
    }


    @Override
    public ActualiteResponseDto getActualiteById(Long id) {

        Actualite actualite =
                actualiteRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Actualité introuvable avec l'id : "
                                                + id
                                )
                        );

        return actualiteResponseMapper.toDto(actualite);
    }


    @Override
    public ActualiteResponseDto modifierActualite(
            Long id,
            ActualiteRequestDto dto
    ) {

        Actualite actualite =
                actualiteRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Actualité introuvable avec l'id : "
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

        actualite.setTitre(dto.getTitre());
        actualite.setCorpsTexte(dto.getCorpsTexte());
        actualite.setCommuneCible(dto.getCommuneCible());
        actualite.setEstUrgent(dto.getEstUrgent());
        actualite.setAuteur(auteur);

        Actualite actualiteModifiee =
                actualiteRepository.save(actualite);

        return actualiteResponseMapper.toDto(
                actualiteModifiee
        );
    }


    @Override
    public void supprimerActualite(Long id) {

        if (!actualiteRepository.existsById(id)) {
            throw new RuntimeException(
                    "Actualité introuvable avec l'id : " + id
            );
        }

        actualiteRepository.deleteById(id);
    }
}