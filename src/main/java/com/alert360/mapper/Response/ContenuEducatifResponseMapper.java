package com.alert360.mapper.Response;

import com.alert360.controller.dto.ContenuEducatifResponseDto;
import com.alert360.entity.ContenuEducatif;
import org.springframework.stereotype.Component;

@Component
public class ContenuEducatifResponseMapper {

    public ContenuEducatifResponseDto toDto(ContenuEducatif contenu) {
        if (contenu == null) {
            return null;
        }

        ContenuEducatifResponseDto dto = new ContenuEducatifResponseDto();

        dto.setIdContenu(contenu.getIdContenu());
        dto.setTitre(contenu.getTitre());
        dto.setTheme(contenu.getTheme());
        dto.setFormat(contenu.getFormat());
        dto.setMediaUrl(contenu.getMediaUrl());
        dto.setDatePublication(contenu.getDatePublication());

        // Extraction de l'ID et du nom complet de l'administrateur auteur
        if (contenu.getAuteur() != null) {
            dto.setAuteurId(contenu.getAuteur().getIdUtilisateur());

            String prenom = contenu.getAuteur().getPrenom() != null ? contenu.getAuteur().getPrenom() : "";
            String nom = contenu.getAuteur().getNom() != null ? contenu.getAuteur().getNom() : "";

            dto.setNomAuteur((prenom + " " + nom).trim());
        }

        return dto;
    }
}