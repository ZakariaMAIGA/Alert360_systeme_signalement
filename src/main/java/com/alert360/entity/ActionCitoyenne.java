package com.alert360.entity;



import com.alert360.entity.enums.EnumStatutAction;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "actions_citoyennes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActionCitoyenne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAction;

    @Column(nullable = false)
    private LocalDateTime dateHeureRendezVous;

    @Column(nullable = false)
    private String lieuRassemblement;

    private Integer nombreParticipantsInscrits = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumStatutAction statutAction = EnumStatutAction.PLANIFIEE;

    // --- RELATIONS ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signalement_id")
    private Signalement signalementOrigine;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "action_participants",
            joinColumns = @JoinColumn(name = "action_id"),
            inverseJoinColumns = @JoinColumn(name = "citoyen_id")
    )
    private Set<Citoyen> participants = new HashSet<>();
}