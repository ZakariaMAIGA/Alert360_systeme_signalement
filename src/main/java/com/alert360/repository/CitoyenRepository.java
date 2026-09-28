package com.alert360.repository;

import com.alert360.entity.Citoyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CitoyenRepository extends JpaRepository<Citoyen, Long> {

    List<Citoyen> findByQuartier (String quartier);

    // Récupérer le classement des citoyens par leur score d'engagement civique
    List<Citoyen> findAllByOrderByPointScoreDesc();

    // Trouver les citoyens participants à une action spécifique
    @Query("SELECT c FROM Citoyen c JOIN c.actionsParticipees a WHERE a.idAction = :idAction")
    List<Citoyen> findParticipantsByActionId(@Param("idAction") Long idAction);
}
