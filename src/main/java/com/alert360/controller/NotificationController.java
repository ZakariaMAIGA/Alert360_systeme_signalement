
package com.alert360.controller;

import com.alert360.controller.dto.NotificationResponse;
;

import com.alert360.service.serviceInter.NotificationService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Récupérer toutes les notifications de l'utilisateur connecté.
     */
    @GetMapping("/mes")
    public ResponseEntity<List<NotificationResponse>> mesNotifications(
            Authentication authentication) {

        return ResponseEntity.ok(
                notificationService.getMesNotifications(
                        authentication.getName()
                )
        );
    }

    /**
     * Récupérer uniquement les notifications non lues.
     */
    @GetMapping("/non-lues")
    public ResponseEntity<List<NotificationResponse>> mesNotificationsNonLues(
            Authentication authentication) {

        return ResponseEntity.ok(
                notificationService.getMesNotificationsNonLues(
                        authentication.getName()
                )
        );
    }

    /**
     * Compter les notifications non lues.
     */
    @GetMapping("/non-lues/count")
    public ResponseEntity<Map<String, Long>> compterNonLues(
            Authentication authentication) {

        Map<String, Long> resultat = new HashMap<>();

        resultat.put(
                "count",
                notificationService.compterNonLues(
                        authentication.getName()
                )
        );

        return ResponseEntity.ok(resultat);
    }

    /**
     * Marquer une notification comme lue.
     */
    @PatchMapping("/{id}/lue")
    public ResponseEntity<NotificationResponse> marquerCommeLue(
            @PathVariable("id") Long idNotification,
            Authentication authentication) {

        return ResponseEntity.ok(
                notificationService.marquerCommeLue(
                        idNotification,
                        authentication.getName()
                )
        );
    }

    /**
     * Marquer toutes les notifications comme lues.
     */
    @PatchMapping("/tout-lu")
    public ResponseEntity<Map<String, Object>> marquerToutesCommeLues(
            Authentication authentication) {

        int nombre = notificationService.marquerToutesCommeLues(
                authentication.getName()
        );

        Map<String, Object> resultat = new HashMap<>();

        resultat.put(
                "message",
                "Notifications marquées comme lues."
        );
        resultat.put(
                "nombreNotificationsModifiees",
                nombre
        );

        return ResponseEntity.ok(resultat);
    }
}
