
package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.NotificationResponse;
import com.alert360.entity.Notification;
import com.alert360.entity.Utilisateur;
import com.alert360.entity.enums.EnumTypeNotification;
import com.alert360.repository.NotificationRepository;
import com.alert360.repository.UtilisateurRepository;


import com.alert360.service.serviceInter.NotificationService;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UtilisateurRepository utilisateurRepository;

    /**
     * Récupérer toutes les notifications de l'utilisateur connecté.
     */
    @Override
    public List<NotificationResponse> getMesNotifications(
            String identifiantConnexion) {

        Utilisateur utilisateur =
                trouverUtilisateur(identifiantConnexion);

        return notificationRepository
                .findByDestinataire_IdUtilisateurOrderByDateCreationDesc(
                        utilisateur.getIdUtilisateur()
                )
                .stream()
                .map(NotificationResponse::fromEntity)
                .toList();
    }

    /**
     * Récupérer uniquement les notifications non lues.
     */
    @Override
    public List<NotificationResponse> getMesNotificationsNonLues(
            String identifiantConnexion) {

        Utilisateur utilisateur =
                trouverUtilisateur(identifiantConnexion);

        return notificationRepository
                .findByDestinataire_IdUtilisateurAndEstLueFalseOrderByDateCreationDesc(
                        utilisateur.getIdUtilisateur()
                )
                .stream()
                .map(NotificationResponse::fromEntity)
                .toList();
    }

    /**
     * Compter les notifications non lues.
     */
    @Override
    public long compterNonLues(String identifiantConnexion) {

        Utilisateur utilisateur =
                trouverUtilisateur(identifiantConnexion);

        return notificationRepository
                .countByDestinataire_IdUtilisateurAndEstLueFalse(
                        utilisateur.getIdUtilisateur()
                );
    }

    /**
     * Marquer une notification comme lue.
     */
    @Override
    @Transactional
    public NotificationResponse marquerCommeLue(
            Long idNotification,
            String identifiantConnexion) {

        Utilisateur utilisateur =
                trouverUtilisateur(identifiantConnexion);

        Notification notification = notificationRepository
                .findByIdNotificationAndDestinataire_IdUtilisateur(
                        idNotification,
                        utilisateur.getIdUtilisateur()
                )
                .orElseThrow(() -> new EntityNotFoundException(
                        "Notification introuvable."
                ));

        notification.setEstLue(true);

        Notification notificationEnregistree =
                notificationRepository.save(notification);

        return NotificationResponse.fromEntity(notificationEnregistree);
    }

    /**
     * Marquer toutes les notifications non lues comme lues.
     *
     * @return nombre de notifications modifiées
     */
    @Override
    @Transactional
    public int marquerToutesCommeLues(
            String identifiantConnexion) {

        Utilisateur utilisateur =
                trouverUtilisateur(identifiantConnexion);

        List<Notification> notifications =
                notificationRepository
                        .findByDestinataire_IdUtilisateurAndEstLueFalseOrderByDateCreationDesc(
                                utilisateur.getIdUtilisateur()
                        );

        notifications.forEach(notification ->
                notification.setEstLue(true)
        );

        notificationRepository.saveAll(notifications);

        return notifications.size();
    }

    /**
     * Créer une notification pour un utilisateur.
     */
    @Override
    @Transactional
    public NotificationResponse creerNotification(
            Utilisateur destinataire,
            EnumTypeNotification type,
            String titre,
            String message,
            String referenceType,
            Long referenceId) {

        if (destinataire == null
                || destinataire.getIdUtilisateur() == null) {
            throw new IllegalArgumentException(
                    "Le destinataire doit être un utilisateur enregistré."
            );
        }

        if (!Boolean.TRUE.equals(destinataire.getEstActif())) {
            throw new IllegalArgumentException(
                    "Impossible de notifier un utilisateur inactif."
            );
        }

        if (type == null) {
            throw new IllegalArgumentException(
                    "Le type de notification est obligatoire."
            );
        }

        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException(
                    "Le titre de la notification est obligatoire."
            );
        }

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException(
                    "Le message de la notification est obligatoire."
            );
        }

        Notification notification = Notification.builder()
                .destinataire(destinataire)
                .type(type)
                .titre(titre)
                .message(message)
                .estLue(false)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .build();

        Notification notificationEnregistree =
                notificationRepository.save(notification);

        return NotificationResponse.fromEntity(notificationEnregistree);
    }

    /**
     * Créer une notification pour plusieurs destinataires actifs.
     * Les utilisateurs en double sont ignorés.
     */
    @Override
    @Transactional
    public void notifierDestinataires(
            Collection<? extends Utilisateur> destinataires,
            EnumTypeNotification type,
            String titre,
            String message,
            String referenceType,
            Long referenceId) {

        if (destinataires == null || destinataires.isEmpty()) {
            return;
        }

        Map<Long, Utilisateur> utilisateursUniques =
                new LinkedHashMap<>();

        for (Utilisateur utilisateur : destinataires) {

            if (utilisateur == null
                    || utilisateur.getIdUtilisateur() == null
                    || !Boolean.TRUE.equals(utilisateur.getEstActif())) {
                continue;
            }

            utilisateursUniques.putIfAbsent(
                    utilisateur.getIdUtilisateur(),
                    utilisateur
            );
        }

        for (Utilisateur utilisateur : utilisateursUniques.values()) {
            creerNotification(
                    utilisateur,
                    type,
                    titre,
                    message,
                    referenceType,
                    referenceId
            );
        }
    }

    /**
     * Retrouver l'utilisateur à partir du téléphone ou de l'e-mail.
     */
    private Utilisateur trouverUtilisateur(
            String identifiantConnexion) {

        if (identifiantConnexion == null
                || identifiantConnexion.isBlank()) {
            throw new AccessDeniedException(
                    "Utilisateur non authentifié."
            );
        }

        return utilisateurRepository
                .findByTelephone(identifiantConnexion)
                .or(() -> utilisateurRepository.findByEmail(
                        identifiantConnexion
                ))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Utilisateur connecté introuvable."
                ));
    }
}
