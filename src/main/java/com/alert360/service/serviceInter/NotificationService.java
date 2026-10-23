
package com.alert360.service.serviceInter;

import com.alert360.controller.dto.NotificationResponse;
import com.alert360.entity.Utilisateur;
import com.alert360.entity.enums.EnumTypeNotification;

import java.util.Collection;
import java.util.List;

public interface NotificationService {

    /**
     * Récupérer toutes les notifications de l'utilisateur connecté.
     */
    List<NotificationResponse> getMesNotifications(
            String identifiantConnexion
    );

    /**
     * Récupérer les notifications non lues de l'utilisateur connecté.
     */
    List<NotificationResponse> getMesNotificationsNonLues(
            String identifiantConnexion
    );

    /**
     * Compter les notifications non lues de l'utilisateur connecté.
     */
    long compterNonLues(
            String identifiantConnexion
    );

    /**
     * Marquer une notification comme lue.
     */
    NotificationResponse marquerCommeLue(
            Long idNotification,
            String identifiantConnexion
    );

    /**
     * Marquer toutes les notifications comme lues.
     *
     * @return nombre de notifications modifiées
     */
    int marquerToutesCommeLues(
            String identifiantConnexion
    );

    /**
     * Créer une notification pour un destinataire.
     */
    NotificationResponse creerNotification(
            Utilisateur destinataire,
            EnumTypeNotification type,
            String titre,
            String message,
            String referenceType,
            Long referenceId
    );

    /**
     * Créer une notification pour plusieurs destinataires.
     */
    void notifierDestinataires(
            Collection<? extends Utilisateur> destinataires,
            EnumTypeNotification type,
            String titre,
            String message,
            String referenceType,
            Long referenceId
    );
}
