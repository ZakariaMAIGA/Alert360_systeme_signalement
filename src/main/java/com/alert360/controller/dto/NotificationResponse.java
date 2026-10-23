
package com.alert360.controller.dto;

import com.alert360.entity.Notification;
import com.alert360.entity.enums.EnumTypeNotification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long idNotification;

    private EnumTypeNotification type;

    private String titre;

    private String message;

    private Boolean estLue;

    private LocalDateTime dateCreation;

    private String referenceType;

    private Long referenceId;

    /**
     * Convertit une entité Notification en DTO.
     */
    public static NotificationResponse fromEntity(
            Notification notification) {

        return NotificationResponse.builder()
                .idNotification(notification.getIdNotification())
                .type(notification.getType())
                .titre(notification.getTitre())
                .message(notification.getMessage())
                .estLue(notification.getEstLue())
                .dateCreation(notification.getDateCreation())
                .referenceType(notification.getReferenceType())
                .referenceId(notification.getReferenceId())
                .build();
    }
}
