package com.alert360.entity;

import com.alert360.entity.enums.EnumTypeStructure;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "structures_competentes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"zoneCouvertureGPS", "agents", "signalementsAssignes"})
public class StructureCompetente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStructure;

    @Column(nullable = false)
    private String nomStructure;

    private String quartier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumTypeStructure typeStructure;

    @Column(columnDefinition = "TEXT")
    private String zoneCouvertureGPS;

    private String telephoneUrgence;

    // --- RELATIONS BIDIRECTIONNELLES ---

    @OneToMany(mappedBy = "structure", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AgentStructure> agents = new ArrayList<>();

    @OneToMany(mappedBy = "structureAssignee", fetch = FetchType.LAZY)
    private List<Signalement> signalementsAssignes = new ArrayList<>();
}
