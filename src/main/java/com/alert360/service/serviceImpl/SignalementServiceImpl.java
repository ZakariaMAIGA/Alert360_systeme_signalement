
package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.AssignationSignalementRequestDto;
import com.alert360.controller.dto.SignalementRequestDto;
import com.alert360.controller.dto.SignalementResponseDto;
import com.alert360.entity.AgentStructure;
import com.alert360.entity.Categorie;
import com.alert360.entity.Citoyen;
import com.alert360.entity.Signalement;
import com.alert360.entity.StructureCompetente;
import com.alert360.entity.Utilisateur;
import com.alert360.entity.enums.EnumRole;
import com.alert360.entity.enums.EnumStatut;
import com.alert360.entity.enums.EnumTypeNotification;
import com.alert360.mapper.Request.SignalementRequestMapper;
import com.alert360.mapper.Response.SignalementResponseMapper;
import com.alert360.repository.AbusRepository;
import com.alert360.repository.AgentStructureRepository;
import com.alert360.repository.CategorieRepository;
import com.alert360.repository.CitoyenRepository;
import com.alert360.repository.SignalementRepository;
import com.alert360.repository.StructureCompetenteRepository;
import com.alert360.repository.UtilisateurRepository;
import com.alert360.service.serviceInter.SignalementService;
import com.alert360.service.serviceInter.NotificationService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class SignalementServiceImpl implements SignalementService {

    private final SignalementRepository signalementRepository;
    private final AbusRepository abusRepository;
    private final CitoyenRepository citoyenRepository;
    private final CategorieRepository categorieRepository;
    private final StructureCompetenteRepository structureCompetenteRepository;
    private final AgentStructureRepository agentStructureRepository;
    private final UtilisateurRepository utilisateurRepository;

    private final SignalementRequestMapper requestMapper;
    private final SignalementResponseMapper responseMapper;

    // Service de notifications
    private final NotificationService notificationService;

    /**
     * Récupérer l'utilisateur actuellement authentifié via le JWT.
     */
    private Utilisateur getUtilisateurConnecte() {
        String identifier = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return utilisateurRepository.findByTelephone(identifier)
                .or(() -> utilisateurRepository.findByEmail(identifier))
                .orElseThrow(() -> new EntityNotFoundException(
                        "Utilisateur introuvable avec l'identifiant : "
                                + identifier
                ));
    }

    // ==========================================================
    // CREER UN SIGNALEMENT
    // ==========================================================

    @Override
    public SignalementResponseDto creerSignalement(
            SignalementRequestDto dto) {

        Utilisateur utilisateurConnecte = getUtilisateurConnecte();

        if (utilisateurConnecte.getRole() != EnumRole.ADMIN
                && !utilisateurConnecte.getIdUtilisateur()
                .equals(dto.getCitoyenId())) {

            throw new AccessDeniedException(
                    "Vous ne pouvez pas créer un signalement pour un autre citoyen."
            );
        }

        Citoyen citoyen = citoyenRepository.findById(dto.getCitoyenId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Citoyen introuvable avec l'ID : "
                                + dto.getCitoyenId()
                ));

        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Catégorie introuvable avec l'ID : "
                                + dto.getCategorieId()
                ));

        Signalement signalement = requestMapper.toEntity(
                dto,
                citoyen,
                categorie
        );

        // Attribution automatique de la structure la plus proche.
        if (signalement.getLocalisation() != null
                && categorie.getTypeStructureCible() != null) {

            String typeCible =
                    categorie.getTypeStructureCible().name();

            structureCompetenteRepository
                    .findNearestStructureByType(
                            signalement.getLocalisation(),
                            typeCible
                    )
                    .ifPresent(signalement::setStructureAssignee);
        }

        Signalement signalementEnregistre =
                signalementRepository.save(signalement);

        // NOTIFICATION : nouveau signalement.
        notifierNouveauSignalement(signalementEnregistre);

        return responseMapper.toDto(signalementEnregistre);
    }

    /**
     * Notifie les administrateurs actifs et le responsable de la structure
     * automatiquement assignée, si une structure est disponible.
     */
    private void notifierNouveauSignalement(Signalement signalement) {

        StructureCompetente structure = signalement.getStructureAssignee();

        // Aucune structure assignée : aucune notification à envoyer.
        if (structure == null) {
            return;
        }

        List<AgentStructure> responsables =
                agentStructureRepository
                        .findByStructureIdStructureAndEstResponsableTrue(
                                structure.getIdStructure()
                        );

        if (responsables == null || responsables.isEmpty()) {
            return;
        }

        String code = signalement.getCodeTrackingUnique() != null
                ? signalement.getCodeTrackingUnique()
                : String.valueOf(signalement.getIdSignalement());

        notificationService.notifierDestinataires(
                responsables,
                EnumTypeNotification.NOUVEAU_SIGNALEMENT,
                "Nouveau signalement",
                "Un nouveau signalement a été créé. Référence : " + code,
                "SIGNALEMENT",
                signalement.getIdSignalement()
        );
    }

    // ==========================================================
    // MODIFIER UN SIGNALEMENT
    // ==========================================================

    @Override
    public SignalementResponseDto modifierSignalement(
            Long idSignalement,
            SignalementRequestDto dto) {

        Signalement signalement = signalementRepository.findById(idSignalement)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Signalement introuvable avec l'ID : "
                                + idSignalement
                ));

        Utilisateur utilisateurConnecte = getUtilisateurConnecte();

        if (utilisateurConnecte.getRole() != EnumRole.ADMIN
                && !signalement.getCitoyen()
                .getIdUtilisateur()
                .equals(utilisateurConnecte.getIdUtilisateur())) {

            throw new AccessDeniedException(
                    "Vous n'êtes pas l'auteur de ce signalement."
            );
        }

        Citoyen citoyen = citoyenRepository.findById(dto.getCitoyenId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Citoyen introuvable avec l'ID : "
                                + dto.getCitoyenId()
                ));

        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Catégorie introuvable avec l'ID : "
                                + dto.getCategorieId()
                ));

        requestMapper.updateEntityFromDto(
                signalement,
                dto,
                citoyen,
                categorie
        );

        if (signalement.getLocalisation() != null
                && categorie.getTypeStructureCible() != null) {

            String typeCible =
                    categorie.getTypeStructureCible().name();

            structureCompetenteRepository
                    .findNearestStructureByType(
                            signalement.getLocalisation(),
                            typeCible
                    )
                    .ifPresentOrElse(
                            signalement::setStructureAssignee,
                            () -> signalement.setStructureAssignee(null)
                    );

        } else {
            signalement.setStructureAssignee(null);
        }

        Signalement signalementModifie =
                signalementRepository.save(signalement);

        return responseMapper.toDto(signalementModifie);
    }

    // ==========================================================
    // CHANGER LE STATUT
    // ==========================================================

    @Override
    public SignalementResponseDto changerStatut(
            Long idSignalement,
            EnumStatut nouveauStatut) {

        Signalement signalement = signalementRepository.findById(idSignalement)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Signalement introuvable avec l'ID : "
                                + idSignalement
                ));

        Utilisateur utilisateurConnecte = getUtilisateurConnecte();

        if (utilisateurConnecte instanceof AgentStructure agentConnecte) {

            if (!Boolean.TRUE.equals(agentConnecte.getEstResponsable())) {
                throw new AccessDeniedException(
                        "Seul le responsable de structure peut confirmer le traitement d'un signalement."
                );
            }

            StructureCompetente structureAssignee =
                    signalement.getStructureAssignee();

            StructureCompetente structureResponsable =
                    agentConnecte.getStructure();

            if (structureAssignee == null
                    || structureResponsable == null
                    || !Objects.equals(
                    structureAssignee.getIdStructure(),
                    structureResponsable.getIdStructure()
            )) {

                throw new AccessDeniedException(
                        "Ce signalement ne relève pas de votre structure."
                );
            }

        } else if (utilisateurConnecte.getRole() != EnumRole.ADMIN) {

            throw new AccessDeniedException(
                    "Vous n'avez pas les droits nécessaires pour modifier le statut."
            );
        }

        // On conserve l'ancien statut pour éviter les notifications inutiles.
        EnumStatut ancienStatut = signalement.getStatut();

        signalement.setStatut(nouveauStatut);

        Signalement signalementModifie =
                signalementRepository.save(signalement);

        // NOTIFICATION : seulement si le statut a réellement changé.
        if (ancienStatut != nouveauStatut) {
            notifierChangementStatut(
                    signalementModifie,
                    ancienStatut,
                    nouveauStatut
            );
        }

        return responseMapper.toDto(signalementModifie);
    }


    /**
     * Informe le citoyen du changement de statut.
     * Informe également l'agent de terrain lorsque la résolution est validée.
     */
    private void notifierChangementStatut(
            Signalement signalement,
            EnumStatut ancienStatut,
            EnumStatut nouveauStatut) {

        String code = signalement.getCodeTrackingUnique() != null
                ? signalement.getCodeTrackingUnique()
                : String.valueOf(signalement.getIdSignalement());

        // 1. Notifier le citoyen du changement de statut.
        Citoyen citoyen = signalement.getCitoyen();

        if (citoyen != null) {
            notificationService.creerNotification(
                    citoyen,
                    EnumTypeNotification.CHANGEMENT_STATUT,
                    "Mise à jour de votre signalement",
                    "Le statut de votre signalement " + code
                            + " est passé de " + ancienStatut
                            + " à " + nouveauStatut + ".",
                    "SIGNALEMENT",
                    signalement.getIdSignalement()
            );
        }

        // 2. Si la résolution vient d'être validée,
        // notifier l'agent qui a réellement soumis la preuve.
        if (ancienStatut == EnumStatut.EN_COURS
                && nouveauStatut == EnumStatut.RESOLU
                && signalement.getPreuveResolution() != null) {

            AgentStructure agentEmetteur =
                    signalement.getPreuveResolution().getAgentEmetteur();

            if (agentEmetteur != null) {
                notificationService.creerNotification(
                        agentEmetteur,
                        EnumTypeNotification.CHANGEMENT_STATUT,
                        "Résolution validée",
                        "La preuve que vous avez soumise pour le signalement "
                                + code + " a été validée. Le signalement est maintenant résolu.",
                        "SIGNALEMENT",
                        signalement.getIdSignalement()
                );
            }
        }
    }

    // ==========================================================
    // ASSIGNER UNE STRUCTURE COMPÉTENTE
    // ==========================================================

    @Override
    public SignalementResponseDto assignerStructure(
            Long idSignalement,
            Long idStructure) {

        Signalement signalement = signalementRepository.findById(idSignalement)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Signalement introuvable avec l'ID : "
                                + idSignalement
                ));

        StructureCompetente structure =
                structureCompetenteRepository.findById(idStructure)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Structure compétente introuvable avec l'ID : "
                                        + idStructure
                        ));

        signalement.setStructureAssignee(structure);

        Signalement signalementModifie =
                signalementRepository.save(signalement);

        return responseMapper.toDto(signalementModifie);
    }

    // ==========================================================
    // ASSIGNER UN AGENT DE TERRAIN (estResponsable = false)
    // ==========================================================

    @Override
    public SignalementResponseDto assignerAgent(
            Long idSignalement,
            AssignationSignalementRequestDto dto) {

        Utilisateur utilisateurConnecte = getUtilisateurConnecte();

        // 1. Contrôle du rôle.
        if (utilisateurConnecte instanceof AgentStructure agentConnecte) {

            if (!Boolean.TRUE.equals(agentConnecte.getEstResponsable())) {
                throw new AccessDeniedException(
                        "Seul le responsable de la structure a le droit d'assigner des signalements."
                );
            }

        } else if (utilisateurConnecte.getRole() != EnumRole.ADMIN) {

            throw new AccessDeniedException(
                    "Vous n'avez pas les droits nécessaires pour effectuer une assignation."
            );
        }

        // 2. Vérification de l'existence du signalement.
        Signalement signalement = signalementRepository.findById(idSignalement)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Signalement introuvable avec l'ID : "
                                + idSignalement
                ));

        // 3. Le signalement doit avoir une structure attribuée.
        if (signalement.getStructureAssignee() == null) {
            throw new IllegalStateException(
                    "Le signalement doit être attribué à une structure compétente avant de pouvoir y affecter un agent."
            );
        }

        if (abusRepository.existsBySignalement_IdSignalement(idSignalement)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Un signalement classé comme abus ne peut pas être attribué à un agent."
            );
        }

        // 4. Vérification de l'existence de l'agent cible.
        AgentStructure agentCible = agentStructureRepository
                .findById(dto.getIdAgent())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Agent terrain introuvable avec l'ID : "
                                + dto.getIdAgent()
                ));

        // 5. L'agent doit appartenir à la même structure.
        if (agentCible.getStructure() == null
                || !Objects.equals(
                agentCible.getStructure().getIdStructure(),
                signalement.getStructureAssignee().getIdStructure()
        )) {

            throw new IllegalArgumentException(
                    "L'agent sélectionné n'appartient pas à la structure responsable de ce signalement."
            );
        }

        if (signalement.getAgentAssigne() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ce signalement est déjà attribué à un agent."
            );
        }

        // 6. Seul un agent de terrain peut être affecté.
        if (Boolean.TRUE.equals(agentCible.getEstResponsable())) {
            throw new IllegalArgumentException(
                    "L'agent cible est déjà un responsable. L'assignation doit cibler un agent de terrain."
            );
        }

        // 7. Affectation et mémorisation de l'ancien statut.
        EnumStatut ancienStatut = signalement.getStatut();

        signalement.setAgentAssigne(agentCible);

        if (ancienStatut == EnumStatut.DECLARE) {
            signalement.setStatut(EnumStatut.EN_COURS);
        }

        Signalement signalementModifie =
                signalementRepository.save(signalement);

        // NOTIFICATION : l'agent de terrain est informé de son affectation.
        String code = signalementModifie.getCodeTrackingUnique() != null
                ? signalementModifie.getCodeTrackingUnique()
                : String.valueOf(signalementModifie.getIdSignalement());

        notificationService.creerNotification(
                agentCible,
                EnumTypeNotification.ATTRIBUTION_SIGNALEMENT,
                "Nouveau signalement attribué",
                "Le signalement " + code
                        + " vous a été attribué pour traitement.",
                "SIGNALEMENT",
                signalementModifie.getIdSignalement()
        );

        // NOTIFICATION : prévenir le citoyen si le statut est passé
        // automatiquement de DECLARE à EN_COURS.
        if (ancienStatut == EnumStatut.DECLARE
                && signalementModifie.getStatut() == EnumStatut.EN_COURS) {

            notifierChangementStatut(
                    signalementModifie,
                    ancienStatut,
                    EnumStatut.EN_COURS
            );
        }

        return responseMapper.toDto(signalementModifie);
    }

    // ==========================================================
    // OBTENIR UN SIGNALEMENT PAR ID
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public SignalementResponseDto obtenirParId(Long idSignalement) {

        Signalement signalement = signalementRepository.findById(idSignalement)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Signalement introuvable avec l'ID : "
                                + idSignalement
                ));

        return responseMapper.toDto(signalement);
    }

    // ==========================================================
    // OBTENIR TOUS LES SIGNALEMENTS
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SignalementResponseDto> obtenirTousLesSignalements() {

        return signalementRepository.findAll()
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }

    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UN CITOYEN
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SignalementResponseDto> obtenirParCitoyen(
            Long idCitoyen) {

        return signalementRepository
                .findByCitoyen_IdUtilisateur(idCitoyen)
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }

    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UNE STRUCTURE
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SignalementResponseDto> obtenirParStructure(
            Long idStructure) {

        return signalementRepository
                .findByStructureAssignee_IdStructure(idStructure)
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }

    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UN AGENT TERRAIN
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SignalementResponseDto> obtenirParAgentAssigne(
            Long idAgent) {

        Utilisateur utilisateurConnecte = getUtilisateurConnecte();

        AgentStructure agent = agentStructureRepository.findById(idAgent)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Agent de structure introuvable avec l'ID : "
                                + idAgent
                ));

        if (utilisateurConnecte instanceof AgentStructure agentConnecte) {

            if (agentConnecte.getStructure() == null
                    || agent.getStructure() == null
                    || !Objects.equals(
                    agentConnecte.getStructure().getIdStructure(),
                    agent.getStructure().getIdStructure()
            )) {

                throw new AccessDeniedException(
                        "Vous ne pouvez consulter que les signalements des agents de votre structure."
                );
            }

        } else if (utilisateurConnecte.getRole() != EnumRole.ADMIN) {

            throw new AccessDeniedException(
                    "Vous n'avez pas les droits nécessaires pour consulter les signalements de cet agent."
            );
        }

        return signalementRepository
                .findByAgentAssigne_IdUtilisateur(idAgent)
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }

    // ==========================================================
    // OBTENIR LES SIGNALEMENTS PAR STATUT
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SignalementResponseDto> obtenirParStatut(
            EnumStatut statut) {

        return signalementRepository.findByStatut(statut)
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }

    // ==========================================================
    // SUPPRIMER UN SIGNALEMENT
    // ==========================================================

    @Override
    public void supprimerSignalement(Long idSignalement) {

        if (!signalementRepository.existsById(idSignalement)) {
            throw new EntityNotFoundException(
                    "Signalement introuvable avec l'ID : "
                            + idSignalement
            );
        }

        signalementRepository.deleteById(idSignalement);
    }
}
