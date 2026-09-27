package com.alert360.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "actualites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Actualite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActualite;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String corpsTexte;

    private String communeCible;

    private Boolean estUrgent = false;

    private LocalDateTime datePublication;

    // --- RELATION ---

    // Auteur de l'actualité (Administrateur)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id", nullable = false)
    private Admin auteur;

    @PrePersist
    protected void onCreate() {
        this.datePublication = LocalDateTime.now();
    }
}