package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.ActualiteRequestDto;
import com.alert360.controller.dto.ActualiteResponseDto;
import com.alert360.entity.Actualite;
import com.alert360.entity.Admin;
import com.alert360.mapper.Request.ActualiteRequestMapper;
import com.alert360.mapper.Response.ActualiteResponseMapper;
import com.alert360.repository.ActualiteRepository;
import com.alert360.repository.AdminRepository;
import com.alert360.security.services.UserDetailsCustom;
import com.alert360.service.serviceInter.ActualiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActualiteServiceImpl implements ActualiteService {

    private final ActualiteRepository actualiteRepository;
    private final AdminRepository adminRepository;
    private final ActualiteRequestMapper actualiteRequestMapper;
    private final ActualiteResponseMapper actualiteResponseMapper;

    // ==========================================
    // METHODE PRIVEE DE RECUPERATION DE L'ADMIN CONNECTE
    // ==========================================
    private Admin getAdminConnecte() {
        UserDetailsCustom userDetails = (UserDetailsCustom) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return adminRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Administrateur connecté introuvable avec l'id : " + userDetails.getId()
                ));
    }

    // ==========================================
    // PUBLIER UNE ACTUALITE
    // ==========================================
    @Override
    @Transactional
    public ActualiteResponseDto publierActualite(ActualiteRequestDto dto) {

        // 1. Récupérer l'administrateur actuellement connecté via le SecurityContext
        Admin auteur = getAdminConnecte();

        // 2. Transformer le DTO en Entity en lui associant l'auteur connecté
        Actualite actualite = actualiteRequestMapper.toEntity(dto, auteur);
        actualite.setAuteur(auteur); // Double sécurité : s'assurer que l'auteur est associé

        // 3. Enregistrer dans la base de données
        Actualite actualiteEnregistree = actualiteRepository.save(actualite);

        // 4. Transformer Entity -> Response DTO (avec nomAdminAuteur et idAdminAuteur)
        return actualiteResponseMapper.toDto(actualiteEnregistree);
    }

    // ==========================================
    // MODIFIER UNE ACTUALITE
    // ==========================================
    @Override
    @Transactional
    public ActualiteResponseDto modifierActualite(Long idActualite, ActualiteRequestDto dto) {

        // 1. Rechercher l'actualité à modifier
        Actualite actualite = actualiteRepository.findById(idActualite)
                .orElseThrow(() -> new RuntimeException(
                        "Actualité introuvable avec l'id : " + idActualite
                ));

        // 2. Récupérer l'administrateur qui fait la modification (l'admin connecté)
        Admin auteur = getAdminConnecte();

        // 3. Mettre à jour les champs de l'actualité
        actualite.setTitre(dto.getTitre());
        actualite.setCorpsTexte(dto.getCorpsTexte());
        actualite.setCommuneCible(dto.getCommuneCible());
        actualite.setEstUrgent(dto.getEstUrgent());
        actualite.setAuteur(auteur);

        // 4. Enregistrer les modifications
        Actualite actualiteModifiee = actualiteRepository.save(actualite);

        // 5. Transformer Entity -> Response DTO
        return actualiteResponseMapper.toDto(actualiteModifiee);
    }

    // ==========================================
    // OBTENIR TOUTES LES ACTUALITES
    // ==========================================
    @Override
    @Transactional(readOnly = true)
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
    @Transactional
    public void supprimerActualite(Long idActualite) {

        if (!actualiteRepository.existsById(idActualite)) {
            throw new RuntimeException(
                    "Actualité introuvable avec l'id : " + idActualite
            );
        }

        actualiteRepository.deleteById(idActualite);
    }
}