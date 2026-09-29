package com.alert360.mapper.Request;


import com.alert360.controller.dto.CitoyenRequestDto;
import com.alert360.entity.Citoyen;
import com.alert360.entity.enums.EnumRole;
import org.springframework.stereotype.Component;

@Component
public class CitoyenRequestMapper {

    public Citoyen toEntity(CitoyenRequestDto dto){
        if (dto == null) return null;

        Citoyen citoyen = new Citoyen();
        citoyen.setNom(dto.getNom());
        citoyen.setPrenom(dto.getPrenom());
        citoyen.setTelephone(dto.getTelephone());
        citoyen.setEmail(dto.getEmail());
        citoyen.setMotDePasse(dto.getMotDePasse());
        citoyen.setQuartier(dto.getQuartier());
        citoyen.setRole(EnumRole.CITOYEN);
        citoyen.setEstActif(true);
        citoyen.setPointScore(0);
        citoyen.setNombreSignalementsInvalides(0);




        return  citoyen;
    }
}
