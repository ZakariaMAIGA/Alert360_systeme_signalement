package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.PreuveResolutionRequestDto;
import com.alert360.controller.dto.PreuveResolutionResponseDto;
import com.alert360.entity.AgentStructure;
import com.alert360.entity.PreuveResolution;
import com.alert360.entity.Signalement;
import com.alert360.entity.Utilisateur;
import com.alert360.entity.enums.EnumRole;
import com.alert360.entity.enums.EnumTypeNotification;
import com.alert360.mapper.Request.PreuveResolutionRequestMapper;
import com.alert360.mapper.Response.PreuveResolutionResponseMapper;
import com.alert360.repository.AgentStructureRepository;
import com.alert360.repository.PreuveResolutionRepository;
import com.alert360.repository.SignalementRepository;
import com.alert360.repository.UtilisateurRepository;
import com.alert360.service.serviceInter.NotificationService;
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
    private final AgentStructureRepository agentStructureRepository;
    private final NotificationService notificationService;
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
    public PreuveResolutionResponseDto ajouterPreuveResolution(
            PreuveResolutionRequestDto dto) {

        System.out.println("\n========== DEBUG PREUVE DE RÉSOLUTION ==========");

        Utilisateur utilisateurConnecte = getUtilisateurConnecte();

        System.out.println("Utilisateur connecté : "
                + utilisateurConnecte.getIdUtilisateur());
        System.out.println("Rôle utilisateur : "
                + utilisateurConnecte.getRole());
        System.out.println("Type utilisateur : "
                + utilisateurConnecte.getClass().getSimpleName());

        Signalement signalement = signalementRepository
                .findById(dto.getIdSignalement())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Signalement introuvable avec l'ID : "
                                + dto.getIdSignalement()));

        System.out.println("ID signalement : "
                + signalement.getIdSignalement());
        System.out.println("Code tracking : "
                + signalement.getCodeTrackingUnique());

        // Vérification des permissions
        if (utilisateurConnecte instanceof AgentStructure agent) {

            System.out.println("ID agent connecté : "
                    + agent.getIdUtilisateur());
            System.out.println("Est responsable : "
                    + agent.getEstResponsable());

            if (agent.getStructure() != null) {
                System.out.println("ID structure de l'agent : "
                        + agent.getStructure().getIdStructure());
            } else {
                System.out.println("L'agent n'est rattaché à aucune structure.");
            }

            if (signalement.getAgentAssigne() != null) {
                System.out.println("ID agent assigné au signalement : "
                        + signalement.getAgentAssigne().getIdUtilisateur());
            } else {
                System.out.println("Aucun agent assigné au signalement.");
            }

            if (signalement.getStructureAssignee() != null) {
                System.out.println("ID structure assignée au signalement : "
                        + signalement.getStructureAssignee().getIdStructure());
            } else {
                System.out.println("Aucune structure assignée au signalement.");
            }
        }

        System.out.println("Vérification des habilitations...");
        verifierHabilitationAgent(utilisateurConnecte, signalement);
        System.out.println("Habilitations validées.");

        // Vérification de l'unicité de la preuve
        if (preuveResolutionRepository
                .findBySignalementIdSignalement(dto.getIdSignalement())
                .isPresent()) {

            System.out.println("ERREUR : une preuve existe déjà.");

            throw new IllegalArgumentException(
                    "Une preuve de résolution existe déjà pour le signalement n° "
                            + dto.getIdSignalement());
        }

        // Création et liaison
        PreuveResolution preuve = requestMapper.toEntity(dto, signalement);

        if (utilisateurConnecte instanceof AgentStructure agentEmetteur) {
            preuve.setAgentEmetteur(agentEmetteur);
        }

        PreuveResolution savedPreuve = preuveResolutionRepository.save(preuve);

        signalement.setPreuveResolution(savedPreuve);
        signalementRepository.save(signalement);

        System.out.println("Preuve enregistrée avec succès : "
                + savedPreuve.getIdPreuve());

        // Notification des responsables de la structure
        if (signalement.getStructureAssignee() != null) {

            Long idStructure =
                    signalement.getStructureAssignee().getIdStructure();

            List<AgentStructure> responsables =
                    agentStructureRepository
                            .findByStructureIdStructureAndEstResponsableTrue(
                                    idStructure);

            System.out.println("Nombre de responsables à notifier : "
                    + responsables.size());

            notificationService.notifierDestinataires(
                    responsables,
                    EnumTypeNotification.PREUVE_RESOLUTION_SOUMISE,
                    "Nouvelle preuve de résolution",
                    "Une preuve a été soumise pour le signalement "
                            + signalement.getCodeTrackingUnique()
                            + ". Veuillez la consulter et valider la résolution.",
                    "SIGNALEMENT",
                    signalement.getIdSignalement());
        }

        System.out.println("========== FIN DEBUG PREUVE ==========\n");

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