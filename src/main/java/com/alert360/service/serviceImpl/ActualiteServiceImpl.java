
package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.ActualiteRequestDto;
import com.alert360.controller.dto.ActualiteResponseDto;
import com.alert360.entity.Actualite;
import com.alert360.entity.Admin;
import com.alert360.entity.Utilisateur;
import com.alert360.entity.enums.EnumRole;
import com.alert360.entity.enums.EnumTypeNotification;
import com.alert360.mapper.Request.ActualiteRequestMapper;
import com.alert360.mapper.Response.ActualiteResponseMapper;
import com.alert360.repository.ActualiteRepository;
import com.alert360.repository.AdminRepository;
import com.alert360.repository.UtilisateurRepository;
import com.alert360.security.services.UserDetailsCustom;
import com.alert360.service.serviceInter.NotificationService;
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

    // Dépendances pour les notifications
    private final NotificationService notificationService;
    private final UtilisateurRepository utilisateurRepository;

    // ==========================================
    // RECUPERER L'ADMINISTRATEUR CONNECTE
    // ==========================================
    private Admin getAdminConnecte() {

        UserDetailsCustom userDetails =
                (UserDetailsCustom) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return adminRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Administrateur connecté introuvable avec l'id : "
                                + userDetails.getId()
                ));
    }

    // ==========================================
    // PUBLIER UNE ACTUALITE
    // ==========================================
    @Override
    @Transactional
    public ActualiteResponseDto publierActualite(ActualiteRequestDto dto) {

        // 1. Récupérer l'administrateur connecté
        Admin auteur = getAdminConnecte();

        // 2. Transformer le DTO en entité
        Actualite actualite = actualiteRequestMapper.toEntity(dto, auteur);
        actualite.setAuteur(auteur);

        // 3. Enregistrer l'actualité
        Actualite actualiteEnregistree =
                actualiteRepository.save(actualite);

        // 4. Notifier les citoyens actifs
        notifierPublicationActualite(actualiteEnregistree);

        // 5. Retourner le DTO de réponse
        return actualiteResponseMapper.toDto(actualiteEnregistree);
    }

    // ==========================================
    // NOTIFIER LA PUBLICATION D'UNE ACTUALITE
    // ==========================================
    private void notifierPublicationActualite(Actualite actualite) {

        // Récupérer les utilisateurs ayant le rôle CITOYEN.
        // NotificationService filtrera les comptes inactifs.
        List<? extends Utilisateur> citoyens =
                utilisateurRepository.findByRole(EnumRole.CITOYEN);

        // Choisir le type de notification selon l'urgence
        EnumTypeNotification typeNotification =
                Boolean.TRUE.equals(actualite.getEstUrgent())
                        ? EnumTypeNotification.ALERTE_URGENTE
                        : EnumTypeNotification.NOUVELLE_ACTUALITE;

        String titreNotification =
                Boolean.TRUE.equals(actualite.getEstUrgent())
                        ? "Alerte urgente"
                        : "Nouvelle actualité";

        String messageNotification =
                Boolean.TRUE.equals(actualite.getEstUrgent())
                        ? "Une alerte urgente vient d'être publiée : "
                          + actualite.getTitre()
                        : "Une nouvelle actualité est disponible : "
                          + actualite.getTitre();

        notificationService.notifierDestinataires(
                citoyens,
                typeNotification,
                titreNotification,
                messageNotification,
                "ACTUALITE",
                actualite.getIdActualite()
        );
    }

    // ==========================================
    // OBTENIR UNE ACTUALITE PAR SON ID
    // ==========================================
    @Override
    @Transactional(readOnly = true)
    public ActualiteResponseDto obtenirActualiteParId(Long idActualite) {

        Actualite actualite = actualiteRepository.findById(idActualite)
                .orElseThrow(() -> new RuntimeException(
                        "Actualité introuvable avec l'id : " + idActualite
                ));

        return actualiteResponseMapper.toDto(actualite);
    }

    // ==========================================
    // MODIFIER UNE ACTUALITE
    // ==========================================
    @Override
    @Transactional
    public ActualiteResponseDto modifierActualite(
            Long idActualite,
            ActualiteRequestDto dto
    ) {

        // 1. Rechercher l'actualité
        Actualite actualite = actualiteRepository.findById(idActualite)
                .orElseThrow(() -> new RuntimeException(
                        "Actualité introuvable avec l'id : " + idActualite
                ));

        // 2. Récupérer l'administrateur connecté
        Admin auteur = getAdminConnecte();

        // 3. Mettre à jour les champs
        actualite.setTitre(dto.getTitre());
        actualite.setCorpsTexte(dto.getCorpsTexte());
        actualite.setCommuneCible(dto.getCommuneCible());
        actualite.setImageUrl(dto.getImageUrl());
        actualite.setEstUrgent(dto.getEstUrgent());
        actualite.setAuteur(auteur);

        // 4. Enregistrer les modifications
        Actualite actualiteModifiee =
                actualiteRepository.save(actualite);

        // 5. Retourner le DTO
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
