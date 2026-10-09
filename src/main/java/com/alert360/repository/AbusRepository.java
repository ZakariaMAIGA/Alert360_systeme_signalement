package com.alert360.repository;

import com.alert360.entity.Abus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbusRepository extends JpaRepository<Abus, Long> {
    boolean existsBySignalement_IdSignalement(Long idSignalement);
    Optional<Abus> findBySignalement_IdSignalement(Long idSignalement);
    List<Abus> findByStructure_IdStructureOrderByDateClassementDesc(Long idStructure);
}
