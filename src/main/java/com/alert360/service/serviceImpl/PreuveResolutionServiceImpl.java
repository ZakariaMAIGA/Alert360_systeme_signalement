package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.PreuveResolutionRequestDto;
import com.alert360.controller.dto.PreuveResolutionResponseDto;
import com.alert360.entity.AgentStructure;
import com.alert360.entity.PreuveResolution;
import com.alert360.entity.Signalement;
import com.alert360.entity.Utilisateur;
import com.alert360.entity.enums.EnumRole;
import com.alert360.mapper.Request.PreuveResolutionRequestMapper;
import com.alert360.mapper.Response.PreuveResolutionResponseMapper;
import com.alert360.repository.PreuveResolutionRepository;
import com.alert360.repository.SignalementRepository;
import com.alert360.repository.UtilisateurRepository;
import com.alert360.service.serviceInter.PreuveResolutionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PreuveResolutionServiceImpl implements PreuveResolutionService {

    private final PreuveResolutionRepository preuveResolutionRepository;
    private final SignalementRepository signalementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PreuveResolutionRequestMapper requestMapper;
    private final PreuveResolutionResponseMapper responseMapper;

    /**
     * Extrait l'identifiant du token JWT (Téléphone ou Email)
     * et récupère l'entité Utilisateur correspondante.
     */
    private Utilisateur getUtilisateurConnecte() {
        String identifier = SecurityContextHolder.getContext().getAuthentication().getName();
        return utilisateurRepository.findByTelephone(identifier)
                .or(() -> utilisateurRepository.findByEmail(identifier))
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur connecté introuvable avec l'identifiant : " + identifier));
    }

    /**
     * Vérifie si l'utilisateur a le droit d'interagir avec la preuve d'un signalement.
     */
    private void verifierHabilitationAgent(Utilisateur utilisateurConnecte, Signalement signalement) {
        if (utilisateurConnecte instanceof AgentStructure agentConnecte) {

            // CAS 1 : Agent de terrain (estResponsable = false)
            if (!Boolean.TRUE.equals(agentConnecte.getEstResponsable())) {
                if (signalement.getAgentAssigne() == null ||
                        !signalement.getAgentAssigne().getIdUtilisateur().equals(agentConnecte.getIdUtilisateur())) {
                    throw new AccessDeniedException("FORBIDDEN : Vous ne pouvez ajouter/modifier une preuve que pour un signalement qui vous est assigné.");
                }
            }
            // CAS 2 : Responsable de structure (estResponsable = true)
            else {
                if (signalement.getStructureAssignee() == null ||
                        !signalement.getStructureAssignee().getIdStructure().equals(agentConnecte.getStructure().getIdStructure())) {
                    throw new AccessDeniedException("FORBIDDEN : Ce signalement ne relève pas de la responsabilité de votre structure.");
                }
            }
        } else if (utilisateurConnecte.getRole() != EnumRole.ADMIN) {
            throw new AccessDeniedException("Vous n'avez pas les autorisations nécessaires pour réaliser cette action.");
        }
    }

    @Override
    public PreuveResolutionResponseDto ajouterPreuveResolution(PreuveResolutionRequestDto dto) {
        Utilisateur utilisateurConnecte = getUtilisateurConnecte();

        Signalement signalement = signalementRepository.findById(dto.getIdSignalement())
                .orElseThrow(() -> new EntityNotFoundException("Signalement introuvable avec l'ID : " + dto.getIdSignalement()));

        // 1. VERIFICATION DE LA PERMISSION
        verifierHabilitationAgent(utilisateurConnecte, signalement);

        // 2. VERIFICATION D'UNICITÉ DE LA PREUVE
        if (preuveResolutionRepository.findBySignalementIdSignalement(dto.getIdSignalement()).isPresent()) {
            throw new IllegalArgumentException("Une preuve de résolution existe déjà pour le signalement n° " + dto.getIdSignalement());
        }

        // 3. CREATION ET LIAISON
        PreuveResolution preuve = requestMapper.toEntity(dto, signalement);
        PreuveResolution savedPreuve = preuveResolutionRepository.save(preuve);

        signalement.setPreuveResolution(savedPreuve);
        signalementRepository.save(signalement);

        return responseMapper.toDto(savedPreuve);
    }

    @Override
    public PreuveResolutionResponseDto modifierPreuveResolution(Long idPreuve, PreuveResolutionRequestDto dto) {
        Utilisateur utilisateurConnecte = getUtilisateurConnecte();

        PreuveResolution preuve = preuveResolutionRepository.findById(idPreuve)
                .orElseThrow(() -> new EntityNotFoundException("Preuve de résolution introuvable avec l'ID : " + idPreuve));

        // VERIFICATION DE LA PERMISSION SUR LE SIGNALEMENT ASSOCIÉ
        if (preuve.getSignalement() != null) {
            verifierHabilitationAgent(utilisateurConnecte, preuve.getSignalement());
        }

        preuve.setTypePreuve(dto.getTypePreuve());
        preuve.setPhotoApresUrl(dto.getPhotoApresUrl());
        preuve.setRapportTexte(dto.getRapportTexte());

        PreuveResolution updatedPreuve = preuveResolutionRepository.save(preuve);
        return responseMapper.toDto(updatedPreuve);
    }

    @Override
    @Transactional(readOnly = true)
    public PreuveResolutionResponseDto obtenirParId(Long idPreuve) {
        PreuveResolution preuve = preuveResolutionRepository.findById(idPreuve)
                .orElseThrow(() -> new EntityNotFoundException("Preuve de résolution introuvable avec l'ID : " + idPreuve));
        return responseMapper.toDto(preuve);
    }

    @Override
    @Transactional(readOnly = true)
    public PreuveResolutionResponseDto obtenirParSignalement(Long idSignalement) {
        PreuveResolution preuve = preuveResolutionRepository.findBySignalementIdSignalement(idSignalement)
                .orElseThrow(() -> new EntityNotFoundException("Aucune preuve de résolution enregistrée pour le signalement ID : " + idSignalement));
        return responseMapper.toDto(preuve);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreuveResolutionResponseDto> obtenirToutesLesPreuves() {
        Utilisateur utilisateurConnecte = getUtilisateurConnecte();
        List<PreuveResolution> preuves;

        if (utilisateurConnecte instanceof AgentStructure agentConnecte) {
            // CAS 1 : Agent de terrain (estResponsable = false) -> Uniquement ses preuves à lui
            if (!Boolean.TRUE.equals(agentConnecte.getEstResponsable())) {
                preuves = preuveResolutionRepository
                        .findBySignalementAgentAssigneIdUtilisateur(agentConnecte.getIdUtilisateur());
            }
            // CAS 2 : Responsable de structure (estResponsable = true) -> Toutes les preuves de sa structure
            else {
                Long idStructure = agentConnecte.getStructure().getIdStructure();
                preuves = preuveResolutionRepository
                        .findBySignalementStructureAssigneeIdStructure(idStructure);
            }
        } else if (utilisateurConnecte.getRole() == EnumRole.ADMIN) {
            // CAS 3 : Admin -> Toutes les preuves du système
            preuves = preuveResolutionRepository.findAll();
        } else {
            throw new AccessDeniedException("Vous n'avez pas l'autorisation d'accéder aux preuves.");
        }

        return preuves.stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void supprimerPreuve(Long idPreuve) {
        Utilisateur utilisateurConnecte = getUtilisateurConnecte();

        PreuveResolution preuve = preuveResolutionRepository.findById(idPreuve)
                .orElseThrow(() -> new EntityNotFoundException("Preuve de résolution introuvable avec l'ID : " + idPreuve));

        if (preuve.getSignalement() != null) {
            verifierHabilitationAgent(utilisateurConnecte, preuve.getSignalement());
            preuve.getSignalement().setPreuveResolution(null);
        }

        preuveResolutionRepository.delete(preuve);
    }
}