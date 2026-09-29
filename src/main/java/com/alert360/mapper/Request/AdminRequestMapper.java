package com.alert360.mapper.Request;


import com.alert360.controller.dto.AdminRequestDto;
import com.alert360.entity.Admin;
import com.alert360.entity.enums.EnumRole;
import org.springframework.stereotype.Component;

@Component
public class AdminRequestMapper {

    public Admin toEntity(AdminRequestDto dto) {
        if (dto == null) return null;

        Admin admin = new Admin();
        admin.setNom(dto.getNom());
        admin.setPrenom(dto.getPrenom());
        admin.setTelephone(dto.getTelephone());
        admin.setEmail(dto.getEmail());
        admin.setMotDePasse(dto.getMotDePasse());
        admin.setRole(EnumRole.ADMIN);
        admin.setEstActif(true);
        return admin;
    }
}
