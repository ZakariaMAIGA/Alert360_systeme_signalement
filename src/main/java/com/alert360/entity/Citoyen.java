package com.alert360.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "citoyens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Citoyen extends Utilisateur{
    @Column(nullable = false)
    private String quartier;

    private Integer pointScore = 0;

    private Integer nombreSignalementsInvalides = 0;

    @ElementCollection
    @CollectionTable(name = "citoyen_badges", joinColumns = @JoinColumn(name = "citoyen_id"))
    @Column(name = "badge")
    private List<String> badgesCiviques = new ArrayList<>();

    // Tous les signalements créés par ce citoyen
    @OneToMany(mappedBy = "citoyen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Signalement> signalements = new ArrayList<>();

    // Toutes les actions citoyennes auxquelles ce citoyen participe
    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    private Set<ActionCitoyenne> actionsParticipees = new HashSet<>();
}
