package com.alert360.mapper.Response;





import com.alert360.controller.dto.AgentStructureResponseDto;
import com.alert360.entity.AgentStructure;
import org.springframework.stereotype.Component;

@Component
public class AgentStructureResponseMapper {

    public AgentStructureResponseDto toDto(AgentStructure entity) {
        if (entity == null) {
            return null;
        }

        AgentStructureResponseDto dto = new AgentStructureResponseDto();
        dto.setIdUtilisateur(entity.getIdUtilisateur());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setTelephone(entity.getTelephone());
        dto.setEmail(entity.getEmail());
        dto.setMatriculeAgent(entity.getMatriculeAgent());
        dto.setEstResponsable(entity.isEstResponsable());
        dto.setRole(entity.getRole());
        dto.setEstActif(entity.getEstActif());
        dto.setDateCreation(entity.getDateCreation());

        // Extraction sécurisée des informations de la structure liée
        if (entity.getStructure() != null) {
            dto.setIdStructure(entity.getStructure().getIdStructure());
            dto.setNomStructure(entity.getStructure().getNomStructure());
        }

        return dto;
    }
}