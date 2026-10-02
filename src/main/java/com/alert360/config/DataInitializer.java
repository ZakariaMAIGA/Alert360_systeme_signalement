package com.alert360.config;

import com.alert360.entity.Categorie;
import com.alert360.entity.enums.EnumTypeStructure;
import com.alert360.repository.CategorieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CategorieRepository categorieRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Initialisation / Mise à jour des catégories d'Alert'360...");

        // Initialisation automatique des catégories et de leurs types de structure cibles
        creerOuMettreAJourCategorie("Voirie/Route", EnumTypeStructure.MAIRIE);
        creerOuMettreAJourCategorie("Eau", EnumTypeStructure.SOMAGEP);
        creerOuMettreAJourCategorie("Électricité", EnumTypeStructure.EDM_SA);
        creerOuMettreAJourCategorie("Sécurité", EnumTypeStructure.POLICE);
        creerOuMettreAJourCategorie("Accident", EnumTypeStructure.SAPEURS_POMPIERS);
        creerOuMettreAJourCategorie("Incendie", EnumTypeStructure.SAPEURS_POMPIERS);
        creerOuMettreAJourCategorie("Inondation", EnumTypeStructure.SAPEURS_POMPIERS);


        log.info("Initialisation des catégories terminée avec succès !");
    }

    private void creerOuMettreAJourCategorie(String nom, EnumTypeStructure typeStructureCible) {
        categorieRepository.findByNom(nom)
                .ifPresentOrElse(
                        categorie -> {
                            // Si la catégorie existe déjà, on met à jour son type_structure_cible si nécessaire
                            if (categorie.getTypeStructureCible() != typeStructureCible) {
                                categorie.setTypeStructureCible(typeStructureCible);
                                categorieRepository.save(categorie);
                                log.info("Catégorie '{}' mise à jour -> Structure : {}", nom, typeStructureCible);
                            }
                        },
                        () -> {
                            // Si elle n'existe pas, on la crée
                            Categorie nouvelleCategorie = new Categorie();
                            nouvelleCategorie.setNom(nom);
                            nouvelleCategorie.setTypeStructureCible(typeStructureCible);
                            categorieRepository.save(nouvelleCategorie);
                            log.info("Nouvelle catégorie créée : '{}' -> Structure : {}", nom, typeStructureCible);
                        }
                );
    }
}