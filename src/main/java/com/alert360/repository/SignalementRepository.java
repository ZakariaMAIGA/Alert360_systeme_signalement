package com.alert360.repository;

import com.alert360.entity.Signalement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignalementRepository extends JpaRepository<Signalement, Long> {

    List<Signalement> findByTypeUrgence(EnumTypeUrgence typeUrgence);

}