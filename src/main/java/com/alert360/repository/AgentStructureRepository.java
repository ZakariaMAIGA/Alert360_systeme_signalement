package com.alert360.repository;

import com.alert360.entity.AgentStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentStructureRepository extends JpaRepository<AgentStructure, Long> {
    Optional<AgentStructure> findByMatriculeAgent(String matriculeAgent);

    boolean existsByMatriculeAgent(String matriculeAgent);

    List<AgentStructure>findByStructureIdStructure(Long idStructure);

    List<AgentStructure> findByStructureIdStructureAndEstResponsableTrue(Long idStructure);
}
