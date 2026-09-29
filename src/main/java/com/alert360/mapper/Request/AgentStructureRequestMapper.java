package com.alert360.mapper.Request;



import com.alert360.controller.dto.AgentStructureRequestDto;
import com.alert360.controller.dto.AgentStructureUpdateDto;
import com.alert360.entity.AgentStructure;
import com.alert360.entity.StructureCompetente;
import com.alert360.entity.enums.EnumRole;
import org.springframework.stereotype.Component;

@Component
public class AgentStructureRequestMapper {

    public AgentStructure toEntity(AgentStructureRequestDto dto, StructureCompetente structure) {

        if (dto == null) return null;

        AgentStructure agent = new AgentStructure();

        agent.setNom(dto.getNom());
        agent.setPrenom(dto.getPrenom());
        agent.setTelephone(dto.getTelephone());
        agent.setEmail(dto.getEmail());
        agent.setMotDePasse(dto.getMotDePasse());
        agent.setMatriculeAgent(dto.getMatriculeAgent());

        // Un agent créé par cette opération
        // n'est pas le responsable de la structure.
        agent.setEstResponsable(false);

        agent.setRole(EnumRole.STRUCTURE);
        agent.setEstActif(false);
        agent.setStructure(structure);

        return agent;
    }

}
