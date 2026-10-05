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
import com.alert360.mapper.Request.SignalementRequestMapper;
import com.alert360.mapper.Response.SignalementResponseMapper;
import com.alert360.repository.AgentStructureRepository;
import com.alert360.repository.CategorieRepository;
import com.alert360.repository.CitoyenRepository;
import com.alert360.repository.SignalementRepository;
import com.alert360.repository.StructureCompetenteRepository;
import com.alert360.repository.UtilisateurRepository;
import com.alert360.service.serviceInter.SignalementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SignalementServiceImpl implements SignalementService {

    private final SignalementRepository signalementRepository;
    private final CitoyenRepository citoyenRepository;
    private final CategorieRepository categorieRepository;
    private final StructureCompetenteRepository structureCompetenteRepository;
    private final AgentStructureRepository agentStructureRepository;
    private final UtilisateurRepository utilisateurRepository;

    private final SignalementRequestMapper requestMapper;
    private final SignalementResponseMapper responseMapper;

    /**
     * Helper pour extraire l'utilisateur actuellement authentifié via SecurityContext (JWT)
     */
    private Utilisateur getUtilisateurConnecte() {
        String identifier = SecurityContextHolder.getContext().getAuthentication().getName();
        return utilisateurRepository.findByTelephone(identifier)
                .or(() -> utilisateurRepository.findByEmail(identifier))
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'identifiant : " + identifier));
    }

    // ==========================================================
    // CREER UN SIGNALEMENT
    // ==========================================================

    @Override
    public SignalementResponseDto creerSignalement(SignalementRequestDto dto) {
        Utilisateur utilisateurConnecte = getUtilisateurConnecte();
        if (utilisateurConnecte.getRole() != EnumRole.ADMIN && !utilisateurConnecte.getIdUtilisateur().equals(dto.getCitoyenId())) {
            throw new AccessDeniedException("Vous ne pouvez pas crÃ©er un signalement pour un autre citoyen.");
        }

        Citoyen citoyen = citoyenRepository.findById(dto.getCitoyenId())
                .orElseThrow(() -> new EntityNotFoundException("Citoyen introuvable avec l'ID : " + dto.getCitoyenId()));

        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new EntityNotFoundException("CatÃ©gorie introuvable avec l'ID : " + dto.getCategorieId()));

        Signalement signalement = requestMapper.toEntity(dto, citoyen, categorie);

        if (signalement.getLocalisation() != null && categorie.getTypeStructureCible() != null) {
            String typeCible = categorie.getTypeStructureCible().name();

            structureCompetenteRepository.findNearestStructureByType(signalement.getLocalisation(), typeCible)
                    .ifPresent(signalement::setStructureAssignee);
        }

        Signalement signalementEnregistre = signalementRepository.save(signalement);
        return responseMapper.toDto(signalementEnregistre);
    }

    // ==========================================================
    // MODIFIER UN SIGNALEMENT
    // ==========================================================

    @Override
    public SignalementResponseDto modifierSignalement(Long idSignalement, SignalementRequestDto dto) {

        Signalement signalement = signalementRepository.findById(idSignalement)
                .orElseThrow(() -> new EntityNotFoundException("Signalement introuvable avec l'ID : " + idSignalement));

        Utilisateur utilisateurConnecte = getUtilisateurConnecte();
        if (utilisateurConnecte.getRole() != EnumRole.ADMIN && !signalement.getCitoyen().getIdUtilisateur().equals(utilisateurConnecte.getIdUtilisateur())) {
            throw new AccessDeniedException("Vous n'Ãªtes pas l'auteur de ce signalement.");
        }

        Citoyen citoyen = citoyenRepository.findById(dto.getCitoyenId())
                .orElseThrow(() -> new EntityNotFoundException("Citoyen introuvable avec l'ID : " + dto.getCitoyenId()));

        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new EntityNotFoundException("CatÃ©gorie introuvable avec l'ID : " + dto.getCategorieId()));

        requestMapper.updateEntityFromDto(signalement, dto, citoyen, categorie);

        if (signalement.getLocalisation() != null && categorie.getTypeStructureCible() != null) {
            String typeCible = categorie.getTypeStructureCible().name();

            structureCompetenteRepository.findNearestStructureByType(signalement.getLocalisation(), typeCible)
                    .ifPresentOrElse(
                            signalement::setStructureAssignee,
                            () -> signalement.setStructureAssignee(null)
                    );
        } else {
            signalement.setStructureAssignee(null);
        }

        Signalement signalementModifie = signalementRepository.save(signalement);
        return responseMapper.toDto(signalementModifie);
    }

    // ==========================================================
    // CHANGER LE STATUT
    // ==========================================================

    @Override
    public SignalementResponseDto changerStatut(Long idSignalement, EnumStatut nouveauStatut) {

        Signalement signalement = signalementRepository.findById(idSignalement)
                .orElseThrow(() -> new EntityNotFoundException("Signalement introuvable avec l'ID : " + idSignalement));

        signalement.setStatut(nouveauStatut);
        Signalement signalementModifie = signalementRepository.save(signalement);

        return responseMapper.toDto(signalementModifie);
    }

    // ==========================================================
    // ASSIGNER UNE STRUCTURE COMPÉTENTE
    // ==========================================================

    @Override
    public SignalementResponseDto assignerStructure(Long idSignalement, Long idStructure) {

        Signalement signalement = signalementRepository.findById(idSignalement)
                .orElseThrow(() -> new EntityNotFoundException("Signalement introuvable avec l'ID : " + idSignalement));

        StructureCompetente structure = structureCompetenteRepository.findById(idStructure)
                .orElseThrow(() -> new EntityNotFoundException("Structure compétente introuvable avec l'ID : " + idStructure));

        signalement.setStructureAssignee(structure);
        Signalement signalementModifie = signalementRepository.save(signalement);

        return responseMapper.toDto(signalementModifie);
    }

    // ==========================================================
    // ASSIGNER UN AGENT DE TERRAIN (estResponsable = false)
    // ==========================================================

    @Override
    public SignalementResponseDto assignerAgent(Long idSignalement, AssignationSignalementRequestDto dto) {

        Utilisateur utilisateurConnecte = getUtilisateurConnecte();

        // 1. Contrôle du rôle : Seul un ADMIN ou un Agent Responsable (estResponsable = true) peut effectuer l'assignation
        if (utilisateurConnecte instanceof AgentStructure agentConnecte) {
            if (!Boolean.TRUE.equals(agentConnecte.getEstResponsable())) {
                throw new AccessDeniedException("FORBIDDEN : Seul le Responsable de la structure a le droit d'assigner des signalements.");
            }
        } else if (utilisateurConnecte.getRole() != EnumRole.ADMIN) {
            throw new AccessDeniedException("Vous n'avez pas les droits nécessaires pour effectuer une assignation.");
        }

        // 2. Vérification de l'existence du signalement
        Signalement signalement = signalementRepository.findById(idSignalement)
                .orElseThrow(() -> new EntityNotFoundException("Signalement introuvable avec l'ID : " + idSignalement));

        // 3. Le signalement doit impérativement avoir une structure attribuée
        if (signalement.getStructureAssignee() == null) {
            throw new IllegalStateException("Le signalement doit être attribué à une structure compétente avant de pouvoir y affecter un agent.");
        }

        // 4. Vérification de l'existence de l'agent destinataire
        AgentStructure agentCible = agentStructureRepository.findById(dto.getIdAgent())
                .orElseThrow(() -> new EntityNotFoundException("Agent terrain introuvable avec l'ID : " + dto.getIdAgent()));

        // 5. Vérification que l'agent cible appartient à la MÊME structure que celle qui traite le signalement
        if (!agentCible.getStructure().getIdStructure().equals(signalement.getStructureAssignee().getIdStructure())) {
            throw new IllegalArgumentException("L'agent sélectionné n'appartient pas à la structure responsable de ce signalement.");
        }

        // 6. Validation métier : On ne peut assigner qu'à un agent de terrain (estResponsable = false)
        if (Boolean.TRUE.equals(agentCible.getEstResponsable())) {
            throw new IllegalArgumentException("L'agent cible est déjà un responsable. L'assignation doit cibler un agent de terrain.");
        }

        // 7. Assignation et bascule automatique de statut en EN_COURS si NOUVEAU
        signalement.setAgentAssigne(agentCible);
        if (signalement.getStatut() == EnumStatut.DECLARE) {
            signalement.setStatut(EnumStatut.EN_COURS);
        }

        Signalement signalementModifie = signalementRepository.save(signalement);
        return responseMapper.toDto(signalementModifie);
    }

    // ==========================================================
    // OBTENIR UN SIGNALEMENT PAR ID
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public SignalementResponseDto obtenirParId(Long idSignalement) {

        Signalement signalement = signalementRepository.findById(idSignalement)
                .orElseThrow(() -> new EntityNotFoundException("Signalement introuvable avec l'ID : " + idSignalement));

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
    public List<SignalementResponseDto> obtenirParCitoyen(Long idCitoyen) {

        return signalementRepository.findByCitoyen_IdUtilisateur(idCitoyen)
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }

    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UNE STRUCTURE
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SignalementResponseDto> obtenirParStructure(Long idStructure) {

        return signalementRepository.findByStructureAssignee_IdStructure(idStructure)
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }

    // ==========================================================
    // OBTENIR LES SIGNALEMENTS D'UN AGENT TERRAIN
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SignalementResponseDto> obtenirParAgentAssigne(Long idAgent) {

        return signalementRepository.findByAgentAssigne_IdUtilisateur(idAgent)
                .stream()
                .map(responseMapper::toDto)
                .toList();
    }

    // ==========================================================
    // OBTENIR LES SIGNALEMENTS PAR STATUT
    // ==========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SignalementResponseDto> obtenirParStatut(EnumStatut statut) {

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
            throw new EntityNotFoundException("Signalement introuvable avec l'ID : " + idSignalement);
        }

        signalementRepository.deleteById(idSignalement);
    }
}