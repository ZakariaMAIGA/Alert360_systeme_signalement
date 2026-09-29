package com.alert360.repository;

import com.alert360.entity.StructureCompetente;
import com.alert360.entity.enums.EnumTypeStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StructureCompetenteRepository extends JpaRepository<StructureCompetente, Long> {

    Optional<StructureCompetente> findByNomStructure(String nomStructure);

    List<StructureCompetente> findByTypeStructure(EnumTypeStructure typeStructure);

    List<StructureCompetente> findByQuartier(String quartier);

}