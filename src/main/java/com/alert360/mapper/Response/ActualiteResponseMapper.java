package com.alert360.mapper.Response;

import com.alert360.controller.dto.ActualiteResponseDto;
import com.alert360.entity.Actualite;
import org.springframework.stereotype.Component;

@Component
public class ActualiteResponseMapper {

    public ActualiteResponseDto toDto(Actualite actualite) {
        if (actualite == null) {
            return null;
        }

        ActualiteResponseDto dto = new ActualiteResponseDto();
        dto.setIdActualite(actualite.getIdActualite());
        dto.setTitre(actualite.getTitre());
        dto.setCorpsTexte(actualite.getCorpsTexte());
        dto.setCommuneCible(actualite.getCommuneCible());
        dto.setEstUrgent(actualite.getEstUrgent());
        dto.setDatePublication(actualite.getDatePublication());

        // Extraction explicite des données de l'auteur
        if (actualite.getAuteur() != null) {
            dto.setIdAdminAuteur(actualite.getAuteur().getIdUtilisateur());

            String prenom = actualite.getAuteur().getPrenom() != null ? actualite.getAuteur().getPrenom() : "";
            String nom = actualite.getAuteur().getNom() != null ? actualite.getAuteur().getNom() : "";
            dto.setNomAdminAuteur((prenom + " " + nom).trim());
        }

        return dto;
    }
}