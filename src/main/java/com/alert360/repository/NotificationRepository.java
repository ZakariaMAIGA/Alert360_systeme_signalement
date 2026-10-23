
package com.alert360.repository;

import com.alert360.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification>
    findByDestinataire_IdUtilisateurOrderByDateCreationDesc(
            Long idUtilisateur
    );

    List<Notification>
    findByDestinataire_IdUtilisateurAndEstLueFalseOrderByDateCreationDesc(
            Long idUtilisateur
    );

    long countByDestinataire_IdUtilisateurAndEstLueFalse(
            Long idUtilisateur
    );

    Optional<Notification>
    findByIdNotificationAndDestinataire_IdUtilisateur(
            Long idNotification,
            Long idUtilisateur
    );
}
