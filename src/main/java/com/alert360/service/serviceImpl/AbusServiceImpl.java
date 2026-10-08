package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.AbusRequestDto;
import com.alert360.controller.dto.AbusResponseDto;
import com.alert360.entity.Abus;
import com.alert360.entity.AgentStructure;
import com.alert360.entity.Signalement;
import com.alert360.entity.Utilisateur;
import com.alert360.entity.enums.EnumRole;
import com.alert360.entity.enums.EnumStatut;
import com.alert360.entity.enums.EnumStatutAbus;
import com.alert360.entity.enums.EnumTypeAbus;
import com.alert360.mapper.Response.AbusResponseMapper;
import com.alert360.repository.AbusRepository;
import com.alert360.repository.SignalementRepository;
import com.alert360.repository.UtilisateurRepository;
import com.alert360.service.serviceInter.AbusService;
import com.alert360.service.serviceInter.AbusUploadService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class AbusServiceImpl implements AbusService {
    private final AbusRepository abusRepository;
    private final SignalementRepository signalementRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AbusResponseMapper responseMapper;
    private final AbusUploadService uploadService;

    private Utilisateur utilisateurConnecte() {
        String identifiant = SecurityContextHolder.getContext().getAuthentication().getName();
        return utilisateurRepository.findByTelephone(identifiant)
                .or(() -> utilisateurRepository.findByEmail(identifiant))
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur connecté introuvable."));
    }

    @Override
    public AbusResponseDto classerCommeAbus(AbusRequestDto dto, MultipartFile pieceJointe) {
        Utilisateur utilisateur = utilisateurConnecte();
        AgentStructure agent = verifierResponsable(utilisateur);
        Signalement signalement = signalementRepository.findById(dto.getIdSignalement())
                .orElseThrow(() -> new EntityNotFoundException("Signalement introuvable."));
        if (signalement.getStructureAssignee() == null ||
                !Objects.equals(signalement.getStructureAssignee().getIdStructure(), agent.getStructure().getIdStructure())) {
            throw new AccessDeniedException("Ce signalement ne relève pas de votre structure.");
        }
        if (signalement.getStatut() == EnumStatut.RESOLU || signalement.getAgentAssigne() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Un signalement résolu ou déjà attribué à un agent ne peut pas être classé comme abus."
            );
        }
        if (abusRepository.existsBySignalement_IdSignalement(signalement.getIdSignalement())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ce signalement est déjà classé comme abus.");
        }

        Abus abus = new Abus();
        abus.setSignalement(signalement);
        abus.setStructure(agent.getStructure());
        abus.setAgentClassement(agent);
        abus.setTypeAbus(dto.getTypeAbus());
        if (dto.getTypeAbus() == EnumTypeAbus.AUTRE) {
            String typePersonnalise = dto.getTypeAbusPersonnalise() == null
                    ? ""
                    : dto.getTypeAbusPersonnalise().trim();
            if (typePersonnalise.isEmpty()) {
                throw new IllegalArgumentException("Veuillez préciser le type d’abus lorsque « Autre » est sélectionné.");
            }
            abus.setTypeAbusPersonnalise(typePersonnalise);
        } else {
            abus.setTypeAbusPersonnalise(null);
        }
        abus.setNiveauGravite(dto.getNiveauGravite());
        abus.setJustification(dto.getJustification().trim());
        if (pieceJointe != null && !pieceJointe.isEmpty()) {
            abus.setPieceJointeUrl(uploadService.enregistrer(pieceJointe));
        }
        return responseMapper.toDto(abusRepository.save(abus));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AbusResponseDto> obtenirLesAbus() {
        Utilisateur utilisateur = utilisateurConnecte();
        List<Abus> abus;
        if (utilisateur instanceof AgentStructure agent) {
            abus = abusRepository.findByStructure_IdStructureOrderByDateClassementDesc(
                    agent.getStructure().getIdStructure()
            );
        } else if (utilisateur.getRole() == EnumRole.ADMIN) {
            abus = abusRepository.findAll();
        } else {
            throw new AccessDeniedException("Vous n’avez pas accès aux abus.");
        }
        return abus.stream().map(responseMapper::toDto).toList();
    }

    @Override
    public AbusResponseDto changerStatut(Long idAbus, EnumStatutAbus statut) {
        if (statut != EnumStatutAbus.CONFIRME) {
            throw new IllegalArgumentException("Le seul changement de statut autorisé est la confirmation de l’abus.");
        }
        Utilisateur utilisateur = utilisateurConnecte();
        Abus abus = abusRepository.findById(idAbus)
                .orElseThrow(() -> new EntityNotFoundException("Abus introuvable."));
        if (utilisateur instanceof AgentStructure agent) {
            if (!Boolean.TRUE.equals(agent.getEstResponsable()) ||
                    !Objects.equals(agent.getStructure().getIdStructure(), abus.getStructure().getIdStructure())) {
                throw new AccessDeniedException("Seul le responsable de la structure concernée peut confirmer cet abus.");
            }
        } else if (utilisateur.getRole() != EnumRole.ADMIN) {
            throw new AccessDeniedException("Vous n’avez pas le droit de modifier le statut de cet abus.");
        }
        abus.getSignalement().setStatut(EnumStatut.REJETE);
        abus.setStatut(statut);
        signalementRepository.save(abus.getSignalement());
        return responseMapper.toDto(abusRepository.save(abus));
    }

    @Override
    @Transactional(readOnly = true)
    public Resource obtenirPieceJointe(Long idAbus) {
        Utilisateur utilisateur = utilisateurConnecte();
        Abus abus = abusRepository.findById(idAbus)
                .orElseThrow(() -> new EntityNotFoundException("Abus introuvable."));
        if (utilisateur instanceof AgentStructure agent) {
            if (!Objects.equals(agent.getStructure().getIdStructure(), abus.getStructure().getIdStructure())) {
                throw new AccessDeniedException("Cette pièce justificative ne relève pas de votre structure.");
            }
        } else if (utilisateur.getRole() != EnumRole.ADMIN) {
            throw new AccessDeniedException("Vous n’avez pas accès à cette pièce justificative.");
        }
        if (abus.getPieceJointeUrl() == null) {
            throw new EntityNotFoundException("Cet abus ne comporte aucune pièce justificative.");
        }
        return uploadService.charger(abus.getPieceJointeUrl());
    }

    private AgentStructure verifierResponsable(Utilisateur utilisateur) {
        if (!(utilisateur instanceof AgentStructure agent) || !Boolean.TRUE.equals(agent.getEstResponsable())) {
            throw new AccessDeniedException("Seul un responsable de structure peut classer un abus.");
        }
        return agent;
    }
}
