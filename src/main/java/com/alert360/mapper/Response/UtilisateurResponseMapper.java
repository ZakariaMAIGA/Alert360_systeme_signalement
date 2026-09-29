package com.alert360.mapper.Response;


import com.alert360.controller.dto.UtilisateurResponseDto;
import com.alert360.entity.Utilisateur;
import org.springframework.stereotype.Component;

@Component
public class UtilisateurResponseMapper {

    public UtilisateurResponseDto toDto (Utilisateur utilisateur) {
        if(utilisateur==null) return null;

        UtilisateurResponseDto dto = new UtilisateurResponseDto();
        dto.setIdUtilisateur(utilisateur.getIdUtilisateur());
        dto.setNom(utilisateur.getNom());
        dto.setPrenom(utilisateur.getPrenom());
        dto.setTelephone(utilisateur.getTelephone());
        dto.setEmail(utilisateur.getEmail());
        dto.setRole(utilisateur.getRole());
        dto.setEstActif(utilisateur.getEstActif());
        dto.setDateCreation(utilisateur.getDateCreation());

        return dto;
    }
}
