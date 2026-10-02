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

    List<StructureCompetente> findByQuartierContainingIgnoreCase(String quartier);

    /**
     * Recherche la structure dont la zone de couverture contient le point GPS fourni.
     */
    @Query(value = """
        SELECT s.* FROM structures_competentes s
        WHERE s.zone_couverturegps IS NOT NULL
          AND ST_Contains(s.zone_couverturegps, :point)
        LIMIT 1
        """, nativeQuery = true)
    Optional<StructureCompetente> findStructureByPointGps(@Param("point") Point point);

    /**
     * Recherche la structure la plus proche du point GPS reçu
     * FILTRÉE par son type de structure (ex: SOMAGEP, EDM_SA, MAIRIE...).
     */
    @Query(value = """
        SELECT s.* FROM structures_competentes s
        WHERE s.zone_couverturegps IS NOT NULL
          AND s.type_structure = :typeStructure
        ORDER BY s.zone_couverturegps <-> :point
        LIMIT 1
        """, nativeQuery = true)
    Optional<StructureCompetente> findNearestStructureByType(
            @Param("point") Point point,
            @Param("typeStructure") String typeStructure
    );
}