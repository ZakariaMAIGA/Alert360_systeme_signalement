package com.alert360.entity;

import com.alert360.entity.enums.EnumFormat;
import com.alert360.entity.enums.EnumThematique;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contenus_educatifs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContenuEducatif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContenu;

    @Column(nullable = false)
    private String titre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumThematique theme;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumFormat format;

    @Column(nullable = false)
    private String mediaUrl;

    private LocalDateTime datePublication;

    // --- RELATION ---

    // Auteur du contenu éducatif (Administrateur)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id", nullable = false)
    private Admin auteur;

    @PrePersist
    protected void onCreate() {
        this.datePublication = LocalDateTime.now();
    }
}