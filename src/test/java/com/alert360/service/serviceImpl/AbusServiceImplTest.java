package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.AbusRequestDto;
import com.alert360.controller.dto.AbusResponseDto;
import com.alert360.entity.Abus;
import com.alert360.entity.AgentStructure;
import com.alert360.entity.Signalement;
import com.alert360.entity.StructureCompetente;
import com.alert360.entity.Utilisateur;
import com.alert360.entity.enums.EnumNiveauGraviteAbus;
import com.alert360.entity.enums.EnumStatut;
import com.alert360.entity.enums.EnumStatutAbus;
import com.alert360.entity.enums.EnumTypeAbus;
import com.alert360.mapper.Response.AbusResponseMapper;
import com.alert360.repository.AbusRepository;
import com.alert360.repository.SignalementRepository;
import com.alert360.repository.UtilisateurRepository;
import com.alert360.service.serviceInter.AbusUploadService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbusServiceImplTest {
    @Mock private AbusRepository abusRepository;
    @Mock private SignalementRepository signalementRepository;
    @Mock private UtilisateurRepository utilisateurRepository;
    @Mock private AbusResponseMapper responseMapper;
    @Mock private AbusUploadService uploadService;

    @InjectMocks
    private AbusServiceImpl service;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void allowsResponsibleToClassifySignalementFromTheirStructure() {
        AgentStructure responsable = agent(4L, true);
        authenticate(responsable);
        Signalement signalement = signalement(4L);
        when(signalement.getIdSignalement()).thenReturn(12L);
        when(signalement.getStatut()).thenReturn(EnumStatut.DECLARE);
        when(signalement.getAgentAssigne()).thenReturn(null);
        when(signalementRepository.findById(12L)).thenReturn(Optional.of(signalement));
        when(abusRepository.existsBySignalement_IdSignalement(12L)).thenReturn(false);
        when(abusRepository.save(any(Abus.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(responseMapper.toDto(any(Abus.class))).thenReturn(new AbusResponseDto());

        AbusRequestDto request = new AbusRequestDto();
        request.setIdSignalement(12L);
        request.setTypeAbus(EnumTypeAbus.FAUSSE_INFORMATION);
        request.setNiveauGravite(EnumNiveauGraviteAbus.MOYEN);
        request.setJustification("Informations non vérifiées");

        service.classerCommeAbus(request, null);

        verify(signalement, never()).setStatut(EnumStatut.REJETE);
        verify(abusRepository).save(argThat(abus ->
                abus.getSignalement() == signalement &&
                        abus.getStructure() == responsable.getStructure() &&
                        abus.getAgentClassement() == responsable &&
                        abus.getStatut().name().equals("A_VERIFIER")
        ));
    }

    @Test
    void storesCustomAbuseTypeWhenOtherIsSelected() {
        AgentStructure responsable = agent(4L, true);
        authenticate(responsable);
        Signalement signalement = signalement(4L);
        when(signalement.getIdSignalement()).thenReturn(12L);
        when(signalement.getStatut()).thenReturn(EnumStatut.DECLARE);
        when(signalement.getAgentAssigne()).thenReturn(null);
        when(signalementRepository.findById(12L)).thenReturn(Optional.of(signalement));
        when(abusRepository.existsBySignalement_IdSignalement(12L)).thenReturn(false);
        when(abusRepository.save(any(Abus.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(responseMapper.toDto(any(Abus.class))).thenReturn(new AbusResponseDto());

        AbusRequestDto request = request(12L);
        request.setTypeAbus(EnumTypeAbus.AUTRE);
        request.setTypeAbusPersonnalise("Usurpation de signalement");

        service.classerCommeAbus(request, null);

        verify(abusRepository).save(argThat(abus ->
                abus.getTypeAbus() == EnumTypeAbus.AUTRE &&
                        "Usurpation de signalement".equals(abus.getTypeAbusPersonnalise())
        ));
    }

    @Test
    void rejectsSignalementWhenAbuseIsConfirmed() {
        AgentStructure responsable = agent(4L, true);
        authenticate(responsable);
        Signalement signalement = mock(Signalement.class);
        Abus abus = mock(Abus.class);
        var structure = responsable.getStructure();
        when(abus.getSignalement()).thenReturn(signalement);
        when(abus.getStructure()).thenReturn(structure);
        when(abusRepository.findById(6L)).thenReturn(Optional.of(abus));
        when(abusRepository.save(abus)).thenReturn(abus);
        when(responseMapper.toDto(abus)).thenReturn(new AbusResponseDto());

        service.changerStatut(6L, EnumStatutAbus.CONFIRME);

        verify(signalement).setStatut(EnumStatut.REJETE);
        verify(signalementRepository).save(signalement);
        verify(abus).setStatut(EnumStatutAbus.CONFIRME);
        verify(abusRepository).save(abus);
    }

    @Test
    void deniesFieldAgentFromClassifyingAbuse() {
        AgentStructure agentTerrain = agent(4L, false);
        authenticate(agentTerrain);

        assertThrows(
                AccessDeniedException.class,
                () -> service.classerCommeAbus(new AbusRequestDto(), null)
        );

        verifyNoInteractions(signalementRepository, abusRepository);
    }

    @Test
    void rejectsResolvedSignalementAsAbuse() {
        AgentStructure responsable = agent(4L, true);
        authenticate(responsable);
        Signalement signalement = signalement(4L);
        when(signalement.getStatut()).thenReturn(EnumStatut.RESOLU);
        when(signalementRepository.findById(12L)).thenReturn(Optional.of(signalement));

        assertThrows(
                ResponseStatusException.class,
                () -> service.classerCommeAbus(request(12L), null)
        );

        verify(abusRepository, never()).save(any(Abus.class));
    }

    @Test
    void rejectsSignalementAlreadyAssignedToAnAgentAsAbuse() {
        AgentStructure responsable = agent(4L, true);
        authenticate(responsable);
        Signalement signalement = signalement(4L);
        when(signalement.getStatut()).thenReturn(EnumStatut.EN_COURS);
        when(signalement.getAgentAssigne()).thenReturn(mock(AgentStructure.class));
        when(signalementRepository.findById(12L)).thenReturn(Optional.of(signalement));

        assertThrows(
                ResponseStatusException.class,
                () -> service.classerCommeAbus(request(12L), null)
        );

        verify(abusRepository, never()).save(any(Abus.class));
    }

    private AbusRequestDto request(Long idSignalement) {
        AbusRequestDto request = new AbusRequestDto();
        request.setIdSignalement(idSignalement);
        request.setTypeAbus(EnumTypeAbus.FAUSSE_INFORMATION);
        request.setNiveauGravite(EnumNiveauGraviteAbus.MOYEN);
        request.setJustification("Informations non vérifiées");
        return request;
    }

    private AgentStructure agent(Long idStructure, boolean responsable) {
        AgentStructure agent = mock(AgentStructure.class);
        when(agent.getEstResponsable()).thenReturn(responsable);
        if (responsable) {
            StructureCompetente structure = mock(StructureCompetente.class);
            when(agent.getStructure()).thenReturn(structure);
            when(structure.getIdStructure()).thenReturn(idStructure);
        }
        return agent;
    }

    private Signalement signalement(Long idStructure) {
        StructureCompetente structure = mock(StructureCompetente.class);
        when(structure.getIdStructure()).thenReturn(idStructure);
        Signalement signalement = mock(Signalement.class);
        when(signalement.getStructureAssignee()).thenReturn(structure);
        return signalement;
    }

    private void authenticate(Utilisateur utilisateur) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("responsable", null)
        );
        when(utilisateurRepository.findByTelephone("responsable")).thenReturn(Optional.of(utilisateur));
    }
}
