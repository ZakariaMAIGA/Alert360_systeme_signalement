package com.alert360.mapper.Response;

import com.alert360.controller.dto.PreuveResolutionResponseDto;
import com.alert360.entity.AgentStructure;
import com.alert360.entity.PreuveResolution;
import com.alert360.entity.Signalement;
import com.alert360.entity.enums.EnumTypePreuve;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PreuveResolutionResponseMapperTest {

    private final PreuveResolutionResponseMapper mapper = new PreuveResolutionResponseMapper();

    @Test
    void mapsAssignedAgentDetailsToProofResponse() {
        AgentStructure agent = new AgentStructure();
        agent.setNom("Traoré");
        agent.setPrenom("Moussa");
        agent.setTelephone("70123456");
        agent.setMatriculeAgent("AG-004");

        Signalement signalement = new Signalement();
        signalement.setIdSignalement(12L);

        PreuveResolution preuve = new PreuveResolution();
        preuve.setIdPreuve(7L);
        preuve.setTypePreuve(EnumTypePreuve.PHOTO_APRES);
        preuve.setSignalement(signalement);
        preuve.setAgentEmetteur(agent);

        PreuveResolutionResponseDto response = mapper.toDto(preuve);

        assertEquals(12L, response.getIdSignalement());
        assertEquals("Traoré", response.getNomAgent());
        assertEquals("Moussa", response.getPrenomAgent());
        assertEquals("70123456", response.getTelephoneAgent());
        assertEquals("AG-004", response.getMatriculeAgent());
    }

    @Test
    void fallsBackToAssignedAgentForExistingProofsWithoutSender() {
        AgentStructure agent = new AgentStructure();
        agent.setNom("Traoré");
        agent.setPrenom("Moussa");
        agent.setTelephone("70123456");
        agent.setMatriculeAgent("AG-004");

        Signalement signalement = new Signalement();
        signalement.setAgentAssigne(agent);

        PreuveResolution preuve = new PreuveResolution();
        preuve.setTypePreuve(EnumTypePreuve.PHOTO_APRES);
        preuve.setSignalement(signalement);

        PreuveResolutionResponseDto response = mapper.toDto(preuve);

        assertEquals("Traoré", response.getNomAgent());
        assertEquals("Moussa", response.getPrenomAgent());
        assertEquals("70123456", response.getTelephoneAgent());
        assertEquals("AG-004", response.getMatriculeAgent());
    }
}
