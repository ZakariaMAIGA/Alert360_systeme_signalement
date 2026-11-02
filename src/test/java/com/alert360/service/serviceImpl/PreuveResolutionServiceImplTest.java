package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.PreuveResolutionResponseDto;
import com.alert360.entity.Citoyen;
import com.alert360.entity.PreuveResolution;
import com.alert360.entity.Signalement;
import com.alert360.mapper.Request.PreuveResolutionRequestMapper;
import com.alert360.mapper.Response.PreuveResolutionResponseMapper;
import com.alert360.repository.PreuveResolutionRepository;
import com.alert360.repository.SignalementRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PreuveResolutionServiceImplTest {
    @Mock private PreuveResolutionRepository preuveResolutionRepository;
    @Mock private SignalementRepository signalementRepository;
    @Mock private UtilisateurRepository utilisateurRepository;
    @Mock private PreuveResolutionRequestMapper requestMapper;
    @Mock private PreuveResolutionResponseMapper responseMapper;

    @InjectMocks
    private PreuveResolutionServiceImpl service;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void citizenCanReadProofForOwnResolvedReport() {
        authenticateCitizen(7L);
        PreuveResolution preuve = proofForCitizen(7L);
        when(preuve.getSignalement().getStatut())
                .thenReturn(com.alert360.entity.enums.EnumStatut.RESOLU);
        PreuveResolutionResponseDto response = new PreuveResolutionResponseDto();
        when(preuveResolutionRepository.findBySignalementIdSignalement(12L))
                .thenReturn(Optional.of(preuve));
        when(responseMapper.toDto(preuve)).thenReturn(response);

        assertSame(response, service.obtenirParSignalement(12L));
    }

    @Test
    void citizenCannotReadProofForAnotherCitizensReport() {
        authenticateCitizen(7L);
        PreuveResolution preuve = proofForCitizen(8L);
        when(preuveResolutionRepository.findBySignalementIdSignalement(12L))
                .thenReturn(Optional.of(preuve));

        assertThrows(AccessDeniedException.class, () -> service.obtenirParSignalement(12L));
        verifyNoInteractions(responseMapper);
    }

    @Test
    void citizenCannotReadProofBeforeReportIsResolved() {
        authenticateCitizen(7L);
        PreuveResolution preuve = proofForCitizen(7L);
        when(preuveResolutionRepository.findBySignalementIdSignalement(12L))
                .thenReturn(Optional.of(preuve));
        when(preuve.getSignalement().getStatut())
                .thenReturn(com.alert360.entity.enums.EnumStatut.EN_COURS);

        assertThrows(AccessDeniedException.class, () -> service.obtenirParSignalement(12L));
        verifyNoInteractions(responseMapper);
    }

    @Test
    void citizenCannotBypassOwnershipCheckByProofId() {
        authenticateCitizen(7L);
        PreuveResolution preuve = proofForCitizen(8L);
        when(preuveResolutionRepository.findById(3L)).thenReturn(Optional.of(preuve));

        assertThrows(AccessDeniedException.class, () -> service.obtenirParId(3L));
        verifyNoInteractions(responseMapper);
    }

    private void authenticateCitizen(Long idCitoyen) {
        Citoyen citoyen = mock(Citoyen.class);
        when(citoyen.getIdUtilisateur()).thenReturn(idCitoyen);
        when(citoyen.getRole()).thenReturn(com.alert360.entity.enums.EnumRole.CITOYEN);
        when(utilisateurRepository.findByTelephone("citoyen"))
                .thenReturn(Optional.of(citoyen));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("citoyen", null)
        );
    }

    private PreuveResolution proofForCitizen(Long idCitoyen) {
        Citoyen proprietaire = mock(Citoyen.class);
        when(proprietaire.getIdUtilisateur()).thenReturn(idCitoyen);

        Signalement signalement = mock(Signalement.class);
        when(signalement.getCitoyen()).thenReturn(proprietaire);

        PreuveResolution preuve = mock(PreuveResolution.class);
        when(preuve.getSignalement()).thenReturn(signalement);
        return preuve;
    }
}
