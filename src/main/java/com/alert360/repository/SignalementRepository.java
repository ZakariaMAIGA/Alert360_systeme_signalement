package com.alert360.repository;

import com.alert360.entity.Signalement;
import com.alert360.entity.enums.EnumStatut;
import com.alert360.entity.enums.EnumTypeUrgence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignalementRepository extends JpaRepository<Signalement, Long> {

    // ==========================================================
    // RECHERCHES PAR URGENCE ET STATUT
    // ==========================================================

    // Rechercher par niveau d'urgence
    List<Signalement> findByTypeUrgence(EnumTypeUrgence typeUrgence);

    // Rechercher par statut
    List<Signalement> findByStatut(EnumStatut statut);

    // Rechercher par urgence ET statut
    List<Signalement> findByTypeUrgenceAndStatut(
            EnumTypeUrgence typeUrgence,
            EnumStatut statut
    );

    // Rechercher les signalements urgents par statut
    List<Signalement> findByTypeUrgenceInAndStatut(
            List<EnumTypeUrgence> typesUrgence,
            EnumStatut statut
    );

    // ==========================================================
    // RECHERCHES PAR ENTITÉS LIÉES (CITOYEN & CATÉGORIE)
    // ==========================================================

    // Rechercher les signalements d'un citoyen
    List<Signalement> findByCitoyen_IdUtilisateur(Long citoyenId);

    // Rechercher les signalements d'une catégorie
    List<Signalement> findByCategorie_IdCategorie(Long categorieId);

    // ==========================================================
    // RECHERCHES PAR STRUCTURE COMPÉTENTE
    // ==========================================================

    // Rechercher les signalements affectés à une structure
    List<Signalement> findByStructureAssignee_IdStructure(Long structureId);

    // Rechercher les signalements d'une structure selon leur statut
    List<Signalement> findByStructureAssignee_IdStructureAndStatut(Long structureId, EnumStatut statut);

    // Rechercher les signalements non encore affectés à une structure
    List<Signalement> findByStructureAssigneeIsNull();

    // Rechercher les signalements affectés à au moins une structure
    List<Signalement> findByStructureAssigneeIsNotNull();

    // ==========================================================
    // RECHERCHES PAR AGENT ASSIGNÉ (TERRAIN & RESPONSABLE)
    // ==========================================================

    // Rechercher les signalements assignés directement à un agent terrain (NÉCESSAIRE POUR SignalementServiceImpl)
    List<Signalement> findByAgentAssigne_IdUtilisateur(Long agentId);

    // Rechercher les signalements d'une structure en attente d'assignation à un agent terrain
    List<Signalement> findByStructureAssignee_IdStructureAndAgentAssigneIsNull(Long structureId);
}