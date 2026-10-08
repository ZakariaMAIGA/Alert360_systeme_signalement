package com.alert360.entity;

import com.alert360.entity.enums.EnumNiveauGraviteAbus;
import com.alert360.entity.enums.EnumStatutAbus;
import com.alert360.entity.enums.EnumTypeAbus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "abus", uniqueConstraints = @UniqueConstraint(columnNames = "signalement_id"))
@Getter
@Setter
@NoArgsConstructor
public class Abus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAbus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signalement_id", nullable = false, unique = true)
    private Signalement signalement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "structure_id", nullable = false)
    private StructureCompetente structure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_classement_id", nullable = false)
    private AgentStructure agentClassement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumTypeAbus typeAbus;

    @Column(name = "type_abus_personnalise", length = 200)
    private String typeAbusPersonnalise;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumNiveauGraviteAbus niveauGravite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumStatutAbus statut = EnumStatutAbus.A_VERIFIER;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String justification;

    private String pieceJointeUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateClassement;

    @PrePersist
    protected void onCreate() {
        dateClassement = LocalDateTime.now();
        if (statut == null) {
            statut = EnumStatutAbus.A_VERIFIER;
        }
    }
}
