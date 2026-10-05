package com.alert360.mapper.Response;

import com.alert360.controller.dto.UtilisateurResponseDto;
import com.alert360.entity.AgentStructure;
import com.alert360.entity.Citoyen;
import com.alert360.entity.Utilisateur;
import org.springframework.stereotype.Component;

@Component
public class UtilisateurResponseMapper {

    public UtilisateurResponseDto toDto(Utilisateur utilisateur) {

        if (utilisateur == null) {
            return null;
        }

        UtilisateurResponseDto dto = new UtilisateurResponseDto();

        // Informations communes
        dto.setIdUtilisateur(utilisateur.getIdUtilisateur());
        dto.setNom(utilisateur.getNom());
        dto.setPrenom(utilisateur.getPrenom());
        dto.setTelephone(utilisateur.getTelephone());
        dto.setEmail(utilisateur.getEmail());
        dto.setRole(utilisateur.getRole());
        dto.setEstActif(utilisateur.getEstActif());
        dto.setDateCreation(utilisateur.getDateCreation());

        // Informations spécifiques à un citoyen
        if (utilisateur instanceof Citoyen citoyen) {
            dto.setQuartier(citoyen.getQuartier());
        }

        // Informations spécifiques à un agent de structure
        if (utilisateur instanceof AgentStructure agent) {

            dto.setMatriculeAgent(agent.getMatriculeAgent());
            dto.setEstResponsable(agent.getEstResponsable());

            if (agent.getStructure() != null) {
                dto.setIdStructure(
                        agent.getStructure().getIdStructure()
                );

                dto.setNomStructure(
                        agent.getStructure().getNomStructure()
                );
            }
        }

        return dto;
    }
}