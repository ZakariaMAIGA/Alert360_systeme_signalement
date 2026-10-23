
package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.ContenuEducatifRequestDto;
import com.alert360.controller.dto.ContenuEducatifResponseDto;
import com.alert360.entity.Admin;
import com.alert360.entity.ContenuEducatif;
import com.alert360.entity.Utilisateur;
import com.alert360.entity.enums.EnumRole;
import com.alert360.entity.enums.EnumTypeNotification;
import com.alert360.mapper.ContenuEducatifRequestMapper;
import com.alert360.mapper.Response.ContenuEducatifResponseMapper;
import com.alert360.repository.AdminRepository;
import com.alert360.repository.ContenuEducatifRepository;
import com.alert360.repository.UtilisateurRepository;
import com.alert360.security.services.UserDetailsCustom;
import com.alert360.service.serviceInter.NotificationService;
import com.alert360.service.serviceInter.ContenuEducatifService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContenuEducatifServiceImpl implements ContenuEducatifService {

    private final ContenuEducatifRepository contenuEducatifRepository;
    private final AdminRepository adminRepository;
    private final ContenuEducatifRequestMapper contenuEducatifRequestMapper;
    private final ContenuEducatifResponseMapper contenuEducatifResponseMapper;

    // Dépendances pour les notifications
    private final NotificationService notificationService;
    private final UtilisateurRepository utilisateurRepository;

    // ==========================================
    // RECUPERER L'ADMIN CONNECTE
    // ==========================================
    private Admin getAdminConnecte() {

        UserDetailsCustom userDetails =
                (UserDetailsCustom) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return adminRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Administrateur introuvable avec l'id : "
                                + userDetails.getId()
                ));
    }

    // ==========================================
    // CREER UN CONTENU EDUCATIF
    // ==========================================
    @Override
    @Transactional
    public ContenuEducatifResponseDto creerContenu(
            ContenuEducatifRequestDto dto
    ) {

        // 1. Récupérer l'administrateur connecté
        Admin auteur = getAdminConnecte();

        // 2. Transformer le DTO en entité
        ContenuEducatif contenu =
                contenuEducatifRequestMapper.toEntity(dto, auteur);

        contenu.setAuteur(auteur);

        // 3. Enregistrer le contenu
        ContenuEducatif contenuEnregistre =
                contenuEducatifRepository.save(contenu);

        // 4. Notifier les citoyens actifs
        notifierNouveauContenu(contenuEnregistre);

        // 5. Retourner le DTO
        return contenuEducatifResponseMapper.toDto(contenuEnregistre);
    }

    // ==========================================
    // NOTIFIER UN NOUVEAU CONTENU EDUCATIF
    // ==========================================
    private void notifierNouveauContenu(ContenuEducatif contenu) {

        List<? extends Utilisateur> citoyens =
                utilisateurRepository.findByRole(EnumRole.CITOYEN);

        String titreNotification = "Nouveau contenu éducatif";

        String messageNotification =
                "Un nouveau contenu éducatif est disponible : "
                        + contenu.getTitre();

        notificationService.notifierDestinataires(
                citoyens,
                EnumTypeNotification.NOUVEAU_CONTENU_EDUCATIF,
                titreNotification,
                messageNotification,
                "CONTENU_EDUCATIF",
                contenu.getIdContenu()
        );
    }

    // ==========================================
    // MODIFIER UN CONTENU EDUCATIF
    // ==========================================
    @Override
    @Transactional
    public ContenuEducatifResponseDto modifierContenu(
            Long idContenu,
            ContenuEducatifRequestDto dto
    ) {

        ContenuEducatif contenu =
                contenuEducatifRepository.findById(idContenu)
                        .orElseThrow(() -> new RuntimeException(
                                "Contenu éducatif introuvable avec l'id : "
                                        + idContenu
                        ));

        Admin auteur = getAdminConnecte();

        contenu.setTitre(dto.getTitre());
        contenu.setTheme(dto.getTheme());
        contenu.setFormat(dto.getFormat());
        contenu.setMediaUrl(dto.getMediaUrl());
        contenu.setAuteur(auteur);

        ContenuEducatif contenuModifie =
                contenuEducatifRepository.save(contenu);

        return contenuEducatifResponseMapper.toDto(contenuModifie);
    }

    // ==========================================
    // OBTENIR UN CONTENU PAR SON ID
    // ==========================================
    @Override
    @Transactional(readOnly = true)
    public ContenuEducatifResponseDto obtenirParId(Long idContenu) {

        ContenuEducatif contenu =
                contenuEducatifRepository.findById(idContenu)
                        .orElseThrow(() -> new RuntimeException(
                                "Contenu éducatif introuvable avec l'id : "
                                        + idContenu
                        ));

        return contenuEducatifResponseMapper.toDto(contenu);
    }

    // ==========================================
    // OBTENIR TOUS LES CONTENUS
    // ==========================================
    @Override
    @Transactional(readOnly = true)
    public List<ContenuEducatifResponseDto> obtenirTousLesContenus() {

        return contenuEducatifRepository
                .findAllByOrderByDatePublicationDesc()
                .stream()
                .map(contenuEducatifResponseMapper::toDto)
                .toList();
    }

    // ==========================================
    // SUPPRIMER UN CONTENU
    // ==========================================
    @Override
    @Transactional
    public void supprimerContenu(Long idContenu) {

        if (!contenuEducatifRepository.existsById(idContenu)) {
            throw new RuntimeException(
                    "Contenu éducatif introuvable avec l'id : "
                            + idContenu
            );
        }

        contenuEducatifRepository.deleteById(idContenu);
    }
}
