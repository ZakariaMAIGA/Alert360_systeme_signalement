package com.alert360.mapper.Response;



import com.alert360.controller.dto.AdminResponseDto;
import com.alert360.entity.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminResponseMapper {

    public AdminResponseDto toDto(Admin entity) {
        if (entity == null) return null;

        AdminResponseDto dto = new AdminResponseDto();
        dto.setIdUtilisateur(entity.getIdUtilisateur());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setTelephone(entity.getTelephone());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        dto.setEstActif(entity.getEstActif());
        dto.setDateCreation(entity.getDateCreation());
        dto.setNombreActualitesPubliees(entity.getActualitesPubliees() != null ? entity.getActualitesPubliees().size() : 0);
        dto.setNombreContenusEducatifsPublies(entity.getContenusEducatifsPublies() != null ? entity.getContenusEducatifsPublies().size() : 0);
        return dto;
    }
}