package com.alert360.repository;

import com.alert360.entity.PreuveResolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreuveResolutionRepository extends JpaRepository<PreuveResolution, Long> {
    Optional<PreuveResolution> findBySignalementIdSignalement(Long idSignalement);

    // Filtre pour l'agent du terrain (ses preuves uniquement)
    List<PreuveResolution> findBySignalementAgentAssigneIdUtilisateur(Long idUtilisateur);

    // Filtre pour le responsable de structure
    List<PreuveResolution> findBySignalementStructureAssigneeIdStructure(Long idStructure);
}
