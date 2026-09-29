package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.ActualiteRequestDto;
import com.alert360.controller.dto.ActualiteResponseDto;
import com.alert360.entity.Actualite;
import com.alert360.entity.Admin;
import com.alert360.mapper.Request.ActualiteRequestMapper;
import com.alert360.mapper.Response.ActualiteResponseMapper;
import com.alert360.repository.ActualiteRepository;
import com.alert360.repository.AdminRepository;
import com.alert360.service.serviceInter.ActualiteService;
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


    // ==========================================
    // PUBLIER UNE ACTUALITE
    // ==========================================
    @Override
    public ActualiteResponseDto publierActualite(
            ActualiteRequestDto dto
    ) {

        // 1. Rechercher l'auteur
        Admin auteur =
                adminRepository.findById(dto.getAuteurId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Administrateur introuvable avec l'id : "
                                                + dto.getAuteurId()
                                )
                        );

        // 2. Transformer le DTO en Entity
        Actualite actualite =
                actualiteRequestMapper.toEntity(
                        dto,
                        auteur
                );

        // 3. Enregistrer dans la base de données
        Actualite actualiteEnregistree =
                actualiteRepository.save(actualite);

        // 4. Transformer Entity -> Response DTO
        return actualiteResponseMapper.toDto(
                actualiteEnregistree
        );
    }


    // ==========================================
    // MODIFIER UNE ACTUALITE
    // ==========================================
    @Override
    public ActualiteResponseDto modifierActualite(
            Long idActualite,
            ActualiteRequestDto dto
    ) {

        // 1. Rechercher l'actualité
        Actualite actualite =
                actualiteRepository.findById(idActualite)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Actualité introuvable avec l'id : "
                                                + idActualite
                                )
                        );

        // 2. Rechercher l'auteur
        Admin auteur =
                adminRepository.findById(dto.getAuteurId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Administrateur introuvable avec l'id : "
                                                + dto.getAuteurId()
                                )
                        );

        // 3. Modifier les informations
        actualite.setTitre(dto.getTitre());
        actualite.setCorpsTexte(dto.getCorpsTexte());
        actualite.setCommuneCible(dto.getCommuneCible());
        actualite.setEstUrgent(dto.getEstUrgent());
        actualite.setAuteur(auteur);

        // 4. Enregistrer les modifications
        Actualite actualiteModifiee =
                actualiteRepository.save(actualite);

        // 5. Transformer Entity -> Response DTO
        return actualiteResponseMapper.toDto(
                actualiteModifiee
        );
    }


    // ==========================================
    // OBTENIR TOUTES LES ACTUALITES
    // ==========================================
    @Override
    public List<ActualiteResponseDto> obtenirToutesLesActualites() {

        return actualiteRepository
                .findAllByOrderByDatePublicationDesc()
                .stream()
                .map(actualiteResponseMapper::toDto)
                .toList();
    }


    // ==========================================
    // SUPPRIMER UNE ACTUALITE
    // ==========================================
    @Override
    public void supprimerActualite(
            Long idActualite
    ) {

        // 1. Vérifier que l'actualité existe
        if (!actualiteRepository.existsById(idActualite)) {

            throw new RuntimeException(
                    "Actualité introuvable avec l'id : "
                            + idActualite
            );
        }

        // 2. Supprimer
        actualiteRepository.deleteById(idActualite);
    }
}