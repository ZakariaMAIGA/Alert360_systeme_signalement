package com.alert360.repository;

import com.alert360.entity.Actualite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActualiteRepository extends JpaRepository<Actualite, Long> {

    // Actualités récentes triées par date de publication décroissante
    List<Actualite> findAllByOrderByDatePublicationDesc();

    List<Actualite> findByEstUrgentTrueOrderByDatePublicationDesc();

    List<Actualite> findByCommuneCibleOrderByDatePublicationDesc(String communeCible);
}