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


    /**
     * Recherche toutes les structures à proximité d'un point GPS dans un rayon donné (en mètres),
     * ordonnées de la plus proche à la plus éloignée.
     * Retourne sous forme d'interface de projection pour récupérer la distance calculée par PostGIS.
     */
    @Query(value = """
        SELECT 
            s.id_structure AS idStructure,
            s.nom_structure AS nomStructure,
            s.quartier AS quartier,
            s.type_structure AS typeStructure,
            s.telephone_urgence AS telephoneUrgence,
            ST_Distance(CAST(s.zone_couverturegps AS geography), CAST(:point AS geography)) AS distanceEnMetres
        FROM structures_competentes s
        WHERE s.zone_couverturegps IS NOT NULL
          AND ST_DWithin(CAST(s.zone_couverturegps AS geography), CAST(:point AS geography), :rayonMetres)
        ORDER BY distanceEnMetres ASC
    """, nativeQuery = true)
    List<SosStructureProjection> findNearbyStructuresForSos(
            @Param("point") Point point,
            @Param("rayonMetres") double rayonMetres
    );

    interface SosStructureProjection {
        Long getIdStructure();
        String getNomStructure();
        String getQuartier();
        String getTypeStructure();
        String getTelephoneUrgence();
        Double getDistanceEnMetres();
    }
}