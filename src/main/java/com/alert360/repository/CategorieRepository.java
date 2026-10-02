package com.alert360.repository;

import com.alert360.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    Optional<Categorie> findByNom(String nom);

    boolean existsByNom(String nom);

    // Optionnel : utile si les noms en BDD diffèrent légèrement au niveau des majuscules/minuscules
    Optional<Categorie> findByNomIgnoreCase(String nom);
}