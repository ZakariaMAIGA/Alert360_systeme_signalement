package com.alert360.repository;

import com.alert360.entity.Signalement;
import com.alert360.entity.enums.EnumStatut;
import com.alert360.entity.enums.EnumTypeUrgence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignalementRepository extends JpaRepository<Signalement, Long> {
    // Rechercher par niveau d'urgence
    List<Signalement> findByTypeUrgence(EnumTypeUrgence typeUrgence);

    // Rechercher par statut
    List<Signalement> findByStatut(EnumStatut statut);

    // Rechercher par urgence ET statut
    List<Signalement> findByTypeUrgenceAndStatut(
            EnumTypeUrgence typeUrgence,
            EnumStatut statut
    );

    // Rechercher les signalements d'un citoyen
    List<Signalement> findByCitoyen_IdUtilisateur(Long citoyenId);

    // Rechercher les signalements d'une catégorie
    List<Signalement> findByCategorie_IdCategorie(Long categorieId);

    // Rechercher les signalements affectés à un agent
    List<Signalement> findByStructureAssignee_IdStructure(
            Long utilisateurId
    );

    // Rechercher les signalements non encore affectés
    List<Signalement> findByStructureAssigneeIsNull();

    // Rechercher les signalements affectés
    List<Signalement> findByStructureAssigneeIsNotNull();

    // Rechercher les signalements urgents par statut
    List<Signalement> findByTypeUrgenceInAndStatut(
            List<EnumTypeUrgence> typesUrgence,
            EnumStatut statut
    );



}