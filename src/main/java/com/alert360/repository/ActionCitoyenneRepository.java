package com.alert360.repository;


import com.alert360.entity.ActionCitoyenne;
import com.alert360.entity.enums.EnumStatutAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActionCitoyenneRepository extends JpaRepository<ActionCitoyenne, Long> {

    List<ActionCitoyenne> findByStatutAction(EnumStatutAction statutAction);

    List<ActionCitoyenne> findBySignalementOrigineIdSignalement(Long idSignalement);

    // Actions citoyennes prévues à venir
    List<ActionCitoyenne> findByDateHeureRendezVousAfterOrderByDateHeureRendezVousAsc(LocalDateTime now);
}
