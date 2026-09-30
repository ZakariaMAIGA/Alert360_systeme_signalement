package com.alert360.entity;

import com.alert360.entity.enums.EnumStatut;
import com.alert360.entity.enums.EnumTypeUrgence;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    // Généré automatiquement avant l'insertion en BDD (ex: ALT-8A2F9B1C)
    @Column(unique = true, nullable = false, updatable = false)
    private String codeTrackingUnique;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumTypeUrgence typeUrgence;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumStatut statut = EnumStatut.DECLARE;

    private String photoAvantUrl;
    private String audioUrl;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    // 📍 Point géospatial PostGIS (Longitude, Latitude) SRID 4326
    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point localisation;

    private String repereVisuel;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateHeureAlerte;

    // --- RELATIONS ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "citoyen_id", nullable = false)
    private Citoyen citoyen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    // Assignée automatiquement par PostGIS dans le Service
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "structure_id")
    private StructureCompetente structureAssignee;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "preuve_resolution_id")
    private PreuveResolution preuveResolution;

    @OneToMany(mappedBy = "signalementOrigine", fetch = FetchType.LAZY)
    private List<ActionCitoyenne> actionsDerivees = new ArrayList<>();

    // --- AUTOMATISATIONS BASE DE DONNÉES ---

    @PrePersist
    protected void onCreate() {
        this.dateHeureAlerte = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = EnumStatut.DECLARE;
        }
        if (this.codeTrackingUnique == null || this.codeTrackingUnique.isEmpty()) {
            this.codeTrackingUnique = "ALT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }
}