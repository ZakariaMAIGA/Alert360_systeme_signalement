package com.alert360.repository;

import com.alert360.entity.StructureCompetente;
import com.alert360.entity.enums.EnumTypeStructure;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StructureCompetenteRepository extends JpaRepository<StructureCompetente, Long> {

    Optional<StructureCompetente> findByNomStructure(String nomStructure);

    List<StructureCompetente> findByTypeStructure(EnumTypeStructure typeStructure);

    List<StructureCompetente> findByQuartier(String quartier);

    //Requête native PostGIS pour trouver la structure la plus proche du point GPS reçu
    @Query(value = """
        SELECT s.* FROM structures_competentes s
        WHERE s.localisation IS NOT NULL
        ORDER BY ST_Distance(s.localisation, :point) ASC
        LIMIT 1
        """, nativeQuery = true)
    Optional<StructureCompetente> findNearestStructure(@Param("point") Point point);

}