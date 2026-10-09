package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.AssignationSignalementRequestDto;
import com.alert360.controller.dto.SignalementResponseDto;
import com.alert360.entity.AgentStructure;
import com.alert360.entity.Signalement;
import com.alert360.entity.StructureCompetente;
import com.alert360.entity.Utilisateur;
import com.alert360.entity.enums.EnumStatut;
import com.alert360.mapper.Request.SignalementRequestMapper;
import com.alert360.mapper.Response.SignalementResponseMapper;
import com.alert360.repository.AbusRepository;
import com.alert360.repository.AgentStructureRepository;
import com.alert360.repository.CategorieRepository;
import com.alert360.repository.CitoyenRepository;
import com.alert360.repository.SignalementRepository;
import com.alert360.repository.StructureCompetenteRepository;
import com.alert360.repository.UtilisateurRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignalementServiceImplTest {

    @Mock private SignalementRepository signalementRepository;
    @Mock private AbusRepository abusRepository;
    @Mock private CitoyenRepository citoyenRepository;
    @Mock private CategorieRepository categorieRepository;
    @Mock private StructureCompetenteRepository structureCompetenteRepository;
    @Mock private AgentStructureRepository agentStructureRepository;
    @Mock private UtilisateurRepository utilisateurRepository;
    @Mock private SignalementRequestMapper requestMapper;
    @Mock private SignalementResponseMapper responseMapper;

    @InjectMocks
    private SignalementServiceImpl service;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void allowsResponsibleToConfirmSignalementFromTheirStructure() {
        AgentStructure responsible = agent(5L, true);
        authenticate(responsible);
        Signalement signalement = signalement(5L);
        when(signalementRepository.findById(20L)).thenReturn(Optional.of(signalement));
        when(signalementRepository.save(signalement)).thenReturn(signalement);
        when(responseMapper.toDto(signalement)).thenReturn(new SignalementResponseDto());

        service.changerStatut(20L, EnumStatut.RESOLU);

        verify(signalement).setStatut(EnumStatut.RESOLU);
        verify(signalementRepository).save(signalement);
    }

    @Test
    void deniesResponsibleFromAnotherStructure() {
        AgentStructure responsible = agent(8L, true);
        authenticate(responsible);
        Signalement signalement = signalement(5L);
        when(signalementRepository.findById(20L)).thenReturn(Optional.of(signalement));

        assertThrows(
                AccessDeniedException.class,
                () -> service.changerStatut(20L, EnumStatut.RESOLU)
        );

        verify(signalementRepository, never()).save(any());
    }

    @Test
    void deniesFieldAgentFromChangingSignalementStatus() {
        AgentStructure fieldAgent = agent(5L, false);
        authenticate(fieldAgent);
        Signalement signalement = mock(Signalement.class);
        when(signalementRepository.findById(20L)).thenReturn(Optional.of(signalement));

        assertThrows(
                AccessDeniedException.class,
                () -> service.changerStatut(20L, EnumStatut.RESOLU)
        );

        verify(signalementRepository, never()).save(any());
    }

    @Test
    void rejectsAssigningSignalementThatAlreadyHasAnAgent() {
        AgentStructure responsible = mock(AgentStructure.class);
        when(responsible.getEstResponsable()).thenReturn(true);
        AgentStructure target = mock(AgentStructure.class);
        StructureCompetente targetStructure = mock(StructureCompetente.class);
        when(target.getStructure()).thenReturn(targetStructure);
        when(targetStructure.getIdStructure()).thenReturn(5L);
        authenticate(responsible);
        Signalement signalement = signalement(5L);
        when(signalementRepository.findById(20L)).thenReturn(Optional.of(signalement));
        when(agentStructureRepository.findById(9L)).thenReturn(Optional.of(target));
        when(signalement.getAgentAssigne()).thenReturn(mock(AgentStructure.class));

        assertThrows(
                ResponseStatusException.class,
                () -> service.assignerAgent(20L, AssignationSignalementRequestDto.builder().idAgent(9L).build())
        );

        verify(signalementRepository, never()).save(any());
    }

    @Test
    void rejectsAssigningSignalementAlreadyClassifiedAsAbuse() {
        AgentStructure responsible = mock(AgentStructure.class);
        when(responsible.getEstResponsable()).thenReturn(true);
        authenticate(responsible);
        Signalement signalement = mock(Signalement.class);
        when(signalement.getStructureAssignee()).thenReturn(mock(StructureCompetente.class));
        when(signalementRepository.findById(20L)).thenReturn(Optional.of(signalement));
        when(abusRepository.existsBySignalement_IdSignalement(20L)).thenReturn(true);

        assertThrows(
                ResponseStatusException.class,
                () -> service.assignerAgent(20L, AssignationSignalementRequestDto.builder().idAgent(9L).build())
        );

        verify(agentStructureRepository, never()).findById(any());
        verify(signalementRepository, never()).save(any());
    }

    private AgentStructure agent(Long idStructure, boolean isResponsible) {
        AgentStructure agent = mock(AgentStructure.class);
        when(agent.getEstResponsable()).thenReturn(isResponsible);
        if (isResponsible) {
            StructureCompetente structure = mock(StructureCompetente.class);
            when(structure.getIdStructure()).thenReturn(idStructure);
            when(agent.getStructure()).thenReturn(structure);
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
