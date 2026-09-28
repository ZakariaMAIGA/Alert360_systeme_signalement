package com.alert360.repository;

import com.alert360.entity.StructureCompetente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StructureCompetenteRepository extends JpaRepository<StructureCompetente, Long> {
}