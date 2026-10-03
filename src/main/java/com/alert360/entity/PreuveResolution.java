package com.alert360.entity;

import com.alert360.entity.enums.EnumTypePreuve;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "preuves_resolution")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreuveResolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPreuve;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumTypePreuve typePreuve;

    private String photoApresUrl;

    @Column(columnDefinition = "TEXT")
    private String rapportTexte;



    private LocalDateTime dateResolution;

    // --- RELATION BIDIRECTIONNELLE ---

    @OneToOne(mappedBy = "preuveResolution", fetch = FetchType.LAZY)
    private Signalement signalement;

    @PrePersist
    protected void onCreate() {
        this.dateResolution = LocalDateTime.now();
    }
}