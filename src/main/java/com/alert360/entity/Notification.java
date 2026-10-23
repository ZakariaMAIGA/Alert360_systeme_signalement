
package com.alert360.entity;

import com.alert360.entity.enums.EnumTypeNotification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(
                        name = "idx_notifications_destinataire",
                        columnList = "destinataire_id"
                ),
                @Index(
                        name = "idx_notifications_destinataire_lue",
                        columnList = "destinataire_id, est_lue"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotification;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "destinataire_id",
            nullable = false
    )
    private Utilisateur destinataire;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "type_notification",
            nullable = false,
            length = 50
    )
    private EnumTypeNotification type;

    @Column(nullable = false, length = 150)
    private String titre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Builder.Default
    @Column(name = "est_lue", nullable = false)
    private Boolean estLue = false;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @PrePersist
    protected void avantInsertion() {
        if (dateCreation == null) {
            dateCreation = LocalDateTime.now();
        }

        if (estLue == null) {
            estLue = false;
        }
    }
}
