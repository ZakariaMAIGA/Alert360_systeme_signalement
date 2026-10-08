package com.alert360.mapper.Response;

import com.alert360.controller.dto.SignalementResponseDto;
import com.alert360.entity.Citoyen;
import com.alert360.entity.Signalement;
import org.springframework.stereotype.Component;

@Component
public class SignalementResponseMapper {

    public SignalementResponseDto toDto(Signalement signalement) {
        if (signalement == null) {
            return null;
        }

        // Extraction sécurisée des coordonnées GPS PostGIS (Y = Latitude, X = Longitude)
        Double latitude = (signalement.getLocalisation() != null) ? signalement.getLocalisation().getY() : null;
        Double longitude = (signalement.getLocalisation() != null) ? signalement.getLocalisation().getX() : null;

        // Formater le nom complet du citoyen en évitant les valeurs "null"
        String citoyenNomComplet = null;
        if (signalement.getCitoyen() != null) {
            Citoyen c = signalement.getCitoyen();
            String prenom = c.getPrenom() != null ? c.getPrenom() : "";
            String nom = c.getNom() != null ? c.getNom() : "";
            citoyenNomComplet = (prenom + " " + nom).trim();
        }

        return SignalementResponseDto.builder()
                .idSignalement(signalement.getIdSignalement())
                .codeTrackingUnique(signalement.getCodeTrackingUnique())
                .typeUrgence(signalement.getTypeUrgence())
                .statut(signalement.getStatut())
                .description(signalement.getDescription())
                .latitudeGPS(latitude)
                .longitudeGPS(longitude)
                .repereVisuel(signalement.getRepereVisuel())
                .photoAvantUrl(signalement.getPhotoAvantUrl())
                .audioUrl(signalement.getAudioUrl())
                .dateHeureAlerte(signalement.getDateHeureAlerte())
                // Citoyen
                .citoyenId(signalement.getCitoyen() != null ? signalement.getCitoyen().getIdUtilisateur() : null)
                .citoyenNomComplet(citoyenNomComplet)
                // Catégorie
                .categorieId(signalement.getCategorie() != null ? signalement.getCategorie().getIdCategorie() : null)
                .categorieNom(signalement.getCategorie() != null ? signalement.getCategorie().getNom() : null)
                // Structure assignée
                .structureAssigneeId(signalement.getStructureAssignee() != null ? signalement.getStructureAssignee().getIdStructure() : null)
                .structureAssigneeNom(signalement.getStructureAssignee() != null ? signalement.getStructureAssignee().getNomStructure() : null)
                .agentAssigneId(signalement.getAgentAssigne() != null ? signalement.getAgentAssigne().getIdUtilisateur() : null)
                .build();
    }
}