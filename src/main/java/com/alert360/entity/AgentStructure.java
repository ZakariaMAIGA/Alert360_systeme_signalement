package com.alert360.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "agents_structure")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AgentStructure extends Utilisateur {

    @Column(unique = true, nullable = false)
    private String matriculeAgent;

    @Column(nullable = false)
    private boolean estResponsable = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "structure_id")
    private StructureCompetente structure;
}
