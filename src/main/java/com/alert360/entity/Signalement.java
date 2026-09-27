package com.alert360.entity;

import com.alert360.entity.enums.EnumStatut;
import com.alert360.entity.enums.EnumTypeUrgence;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "signalements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Signalement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSignalement;

    @Column(unique = true, nullable = false)
    private String codeTrackingUnique;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumTypeUrgence typeUrgence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumStatut statut = EnumStatut.DECLARE;

    private String photoAvantUrl;
    private String audioUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double latitudeGPS;

    @Column(nullable = false)
    private Double longitudeGPS;

    private String repereVisuel;

    @Column(nullable = false)
    private LocalDateTime dateHeureAlerte;

    // --- RELATIONS ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citoyen_id", nullable = false)
    private Citoyen citoyen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "structure_id")
    private StructureCompetente structureAssignee;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "preuve_resolution_id")
    private PreuveResolution preuveResolution;

    // --- RELATION BIDIRECTIONNELLE ---

    @OneToMany(mappedBy = "signalementOrigine", fetch = FetchType.LAZY)
    private List<ActionCitoyenne> actionsDerivees = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.dateHeureAlerte = LocalDateTime.now();
    }
}