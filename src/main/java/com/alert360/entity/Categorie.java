package com.alert360.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategorie;

    @Column(nullable = false, unique = true)
    private String nom;

    // --- RELATION BIDIRECTIONNELLE ---

    @OneToMany(mappedBy = "categorie", fetch = FetchType.LAZY)
    private List<Signalement> signalements = new ArrayList<>();
}
