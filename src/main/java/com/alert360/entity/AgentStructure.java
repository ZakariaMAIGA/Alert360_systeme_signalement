package com.alert360.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "agents_structure")
@PrimaryKeyJoinColumn(name = "id_utilisateur") // Assure la liaison correcte de la clé primaire avec Utilisateur (Stratégie JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentStructure extends Utilisateur {

    @Column(name = "matricule_agent", unique = true, nullable = false)
    private String matriculeAgent;

    @Column(name = "est_responsable", nullable = false)
    private Boolean estResponsable = false; // Initialisé à false par défaut pour éviter de nommer responsable un agent non désigné

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_structure", nullable = false)
    private StructureCompetente structure;
}