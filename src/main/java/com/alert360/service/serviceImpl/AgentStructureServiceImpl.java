package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.AgentStructureRequestDto;
import com.alert360.controller.dto.AgentStructureResponseDto;
import com.alert360.entity.AgentStructure;
import com.alert360.entity.StructureCompetente;
import com.alert360.entity.Utilisateur;
import com.alert360.entity.enums.EnumRole;
import com.alert360.mapper.Request.AgentStructureRequestMapper;
import com.alert360.mapper.Response.AgentStructureResponseMapper;
import com.alert360.repository.AgentStructureRepository;
import com.alert360.repository.StructureCompetenteRepository;
import com.alert360.repository.UtilisateurRepository;
import com.alert360.service.serviceInter.AgentStructureService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AgentStructureServiceImpl implements AgentStructureService {

    private final AgentStructureRepository agentStructureRepository;
    private final StructureCompetenteRepository structureCompetenteRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final AgentStructureRequestMapper requestMapper;
    private final AgentStructureResponseMapper responseMapper;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public AgentStructureResponseDto creerAgent(AgentStructureRequestDto dto) {

        // 1. Identification sécurisée par Téléphone / Email
        Utilisateur utilisateurConnecte = getUtilisateurConnecte();

        // 2. CONTRÔLE D'HABILITATION STRICT & AFFECTATION DE EST_RESPONSABLE
        if (utilisateurConnecte.getRole() == EnumRole.ADMIN) {
            // L'ADMIN peut choisir, ou par défaut s'il ne précise pas -> Responsable (true)
            if (dto.getEstResponsable() == null) {
                dto.setEstResponsable(true);
            }
        } else if (utilisateurConnecte instanceof AgentStructure agentConnecte) {

            // SI L'AGENT CONNECTÉ N'EST PAS RESPONSABLE -> BLOQUER (403)
            if (!Boolean.TRUE.equals(agentConnecte.getEstResponsable())) {
                throw new AccessDeniedException("FORBIDDEN : Seul le Responsable de la structure a l'autorisation de créer des agents.");
            }

            // LE RESPONSABLE CRÉE OBLIGATOIREMENT DES AGENTS TERRAIN (non responsables)
            dto.setEstResponsable(false);

            // RATTRACHEMENT AUTOMATIQUE : Même structure que le responsable
            dto.setIdStructure(agentConnecte.getStructure().getIdStructure());

        } else {
            throw new AccessDeniedException("Vous n'avez pas les droits nécessaires pour effectuer cette action.");
        }

        // 3. Validation des contraintes d'unicité (Matricule & Email)
        if (agentStructureRepository.existsByMatriculeAgent(dto.getMatriculeAgent())) {
            throw new IllegalArgumentException("Un agent existe déjà avec le matricule : " + dto.getMatriculeAgent());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank() && agentStructureRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur existe déjà avec cet email : " + dto.getEmail());
        }

        // 4. Extraction et vérification de la structure
        Long idStructure = dto.getIdStructure();
        StructureCompetente structure = structureCompetenteRepository.findById(idStructure)
                .orElseThrow(() -> new EntityNotFoundException("Structure compétente introuvable avec l'ID : " + idStructure));

        // 5. Mapping DTO -> Entité
        AgentStructure agent = requestMapper.toEntity(dto, structure);

        // FORCER EXPLICITEMENT LE BOOELAN SUR L'ENTITÉ (Évite que le Mapper réinitialise la valeur)
        agent.setEstResponsable(Boolean.TRUE.equals(dto.getEstResponsable()));
        agent.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));

        // S'assurer que le rôle est positionné
        if (agent.getRole() == null) {
            agent.setRole(EnumRole.STRUCTURE);
        }

        // 6. Sauvegarde
        AgentStructure savedAgent = agentStructureRepository.save(agent);
        return responseMapper.toDto(savedAgent);
    }

    @Override
    public AgentStructureResponseDto modifierAgent(Long idUtilisateur, AgentStructureRequestDto dto) {

        Utilisateur utilisateurConnecte = getUtilisateurConnecte();

        AgentStructure agent = agentStructureRepository.findById(idUtilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Agent de structure introuvable avec l'ID : " + idUtilisateur));

        if (utilisateurConnecte instanceof AgentStructure agentConnecte) {
            if (!Boolean.TRUE.equals(agentConnecte.getEstResponsable())) {
                throw new AccessDeniedException("FORBIDDEN : Un agent simple ne peut pas modifier de compte.");
            }
            if (!agentConnecte.getStructure().getIdStructure().equals(agent.getStructure().getIdStructure())) {
                throw new AccessDeniedException("FORBIDDEN : Vous ne pouvez modifier que les agents appartenant à votre structure.");
            }
            dto.setEstResponsable(false);
        }

        if (!agent.getMatriculeAgent().equals(dto.getMatriculeAgent())
                && agentStructureRepository.existsByMatriculeAgent(dto.getMatriculeAgent())) {
            throw new IllegalArgumentException("Un autre agent possède déjà le matricule : " + dto.getMatriculeAgent());
        }

        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(agent.getEmail())
                && agentStructureRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Un autre utilisateur possède déjà cet email : " + dto.getEmail());
        }

        if (utilisateurConnecte.getRole() == EnumRole.ADMIN && dto.getIdStructure() != null
                && !dto.getIdStructure().equals(agent.getStructure().getIdStructure())) {
            StructureCompetente nouvelleStructure = structureCompetenteRepository.findById(dto.getIdStructure())
                    .orElseThrow(() -> new EntityNotFoundException("Structure compétente introuvable avec l'ID : " + dto.getIdStructure()));
            agent.setStructure(nouvelleStructure);
        }

        agent.setNom(dto.getNom());
        agent.setPrenom(dto.getPrenom());
        agent.setTelephone(dto.getTelephone());
        agent.setEmail(dto.getEmail());

        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isBlank()) {
            agent.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        }

        agent.setMatriculeAgent(dto.getMatriculeAgent());

        if (utilisateurConnecte.getRole() == EnumRole.ADMIN && dto.getEstResponsable() != null) {
            agent.setEstResponsable(dto.getEstResponsable());
        }

        AgentStructure updatedAgent = agentStructureRepository.save(agent);
        return responseMapper.toDto(updatedAgent);
    }

    @Override
    @Transactional(readOnly = true)
    public AgentStructureResponseDto obtenirParId(Long idUtilisateur) {
        AgentStructure agent = agentStructureRepository.findById(idUtilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Agent de structure introuvable avec l'ID : " + idUtilisateur));
        return responseMapper.toDto(agent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentStructureResponseDto> obtenirTousLesAgents() {
        return agentStructureRepository.findAll().stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentStructureResponseDto> obtenirAgentsParStructure(Long idStructure) {
        if (!structureCompetenteRepository.existsById(idStructure)) {
            throw new EntityNotFoundException("Structure compétente introuvable avec l'ID : " + idStructure);
        }

        return agentStructureRepository.findByStructureIdStructure(idStructure).stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentStructureResponseDto> obtenirResponsablesParStructure(Long idStructure) {
        if (!structureCompetenteRepository.existsById(idStructure)) {
            throw new EntityNotFoundException("Structure compétente introuvable avec l'ID : " + idStructure);
        }

        return agentStructureRepository.findByStructureIdStructureAndEstResponsableTrue(idStructure).stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AgentStructureResponseDto obtenirParMatricule(String matricule) {
        AgentStructure agent = agentStructureRepository.findByMatriculeAgent(matricule)
                .orElseThrow(() -> new EntityNotFoundException("Agent introuvable avec le matricule : " + matricule));
        return responseMapper.toDto(agent);
    }

    @Override
    public void supprimerAgent(Long idUtilisateur) {
        Utilisateur utilisateurConnecte = getUtilisateurConnecte();

        AgentStructure agentASupprimer = agentStructureRepository.findById(idUtilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Agent de structure introuvable avec l'ID : " + idUtilisateur));

        if (utilisateurConnecte instanceof AgentStructure agentConnecte) {
            if (!Boolean.TRUE.equals(agentConnecte.getEstResponsable())) {
                throw new AccessDeniedException("FORBIDDEN : Un agent simple ne peut pas supprimer de compte.");
            }
            if (!agentConnecte.getStructure().getIdStructure().equals(agentASupprimer.getStructure().getIdStructure())) {
                throw new AccessDeniedException("FORBIDDEN : Vous ne pouvez supprimer que les agents appartenant à votre structure.");
            }
        }

        agentStructureRepository.deleteById(idUtilisateur);
    }
}