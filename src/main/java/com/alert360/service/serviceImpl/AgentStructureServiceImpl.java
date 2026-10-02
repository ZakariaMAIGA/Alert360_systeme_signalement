package com.alert360.service.serviceImpl;

import com.alert360.controller.dto.AgentStructureRequestDto;
import com.alert360.controller.dto.AgentStructureResponseDto;
import com.alert360.entity.AgentStructure;
import com.alert360.entity.StructureCompetente;
import com.alert360.mapper.Request.AgentStructureRequestMapper;
import com.alert360.mapper.Response.AgentStructureResponseMapper;
import com.alert360.repository.AgentStructureRepository;
import com.alert360.repository.StructureCompetenteRepository;
import com.alert360.service.serviceInter.AgentStructureService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AgentStructureServiceImpl implements AgentStructureService {

    private final AgentStructureRepository agentStructureRepository;
    private final StructureCompetenteRepository structureCompetenteRepository;
    private final AgentStructureRequestMapper requestMapper;
    private final AgentStructureResponseMapper responseMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AgentStructureResponseDto creerAgent(AgentStructureRequestDto dto) {
        // 1. Validation des contraintes d'unicité (Matricule & Email)
        if (agentStructureRepository.existsByMatriculeAgent(dto.getMatriculeAgent())) {
            throw new IllegalArgumentException("Un agent existe déjà avec le matricule : " + dto.getMatriculeAgent());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank() && agentStructureRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur existe déjà avec cet email : " + dto.getEmail());
        }

        // 2. Extraction de l'ID de structure depuis le DTO
        Long idStructure = dto.getIdStructure();
        StructureCompetente structure = structureCompetenteRepository.findById(idStructure)
                .orElseThrow(() -> new EntityNotFoundException("Structure compétente introuvable avec l'ID : " + idStructure));

        // 3. Mapping DTO -> Entité
        AgentStructure agent = requestMapper.toEntity(dto, structure);

        // 4. Hashage sécurisé du mot de passe
        agent.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));

        // 5. Sauvegarde
        AgentStructure savedAgent = agentStructureRepository.save(agent);

        return responseMapper.toDto(savedAgent);
    }

    @Override
    public AgentStructureResponseDto modifierAgent(Long idUtilisateur, AgentStructureRequestDto dto) {
        AgentStructure agent = agentStructureRepository.findById(idUtilisateur)
                .orElseThrow(() -> new EntityNotFoundException("Agent de structure introuvable avec l'ID : " + idUtilisateur));

        // Validation d'unicité du matricule s'il a changé
        if (!agent.getMatriculeAgent().equals(dto.getMatriculeAgent())
                && agentStructureRepository.existsByMatriculeAgent(dto.getMatriculeAgent())) {
            throw new IllegalArgumentException("Un autre agent possède déjà le matricule : " + dto.getMatriculeAgent());
        }

        // Validation d'unicité de l'email s'il a changé
        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(agent.getEmail())
                && agentStructureRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Un autre utilisateur possède déjà cet email : " + dto.getEmail());
        }

        // Mise à jour de la structure rattachée si elle change
        if (dto.getIdStructure() != null && !dto.getIdStructure().equals(agent.getStructure().getIdStructure())) {
            StructureCompetente nouvelleStructure = structureCompetenteRepository.findById(dto.getIdStructure())
                    .orElseThrow(() -> new EntityNotFoundException("Structure compétente introuvable avec l'ID : " + dto.getIdStructure()));
            agent.setStructure(nouvelleStructure);
        }

        agent.setNom(dto.getNom());
        agent.setPrenom(dto.getPrenom());
        agent.setTelephone(dto.getTelephone());
        agent.setEmail(dto.getEmail());

        // Hashage du mot de passe uniquement si un nouveau mot de passe est transmis
        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isBlank()) {
            agent.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        }

        agent.setMatriculeAgent(dto.getMatriculeAgent());

        if (dto.getEstResponsable() != null) {
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
        if (!agentStructureRepository.existsById(idUtilisateur)) {
            throw new EntityNotFoundException("Agent de structure introuvable avec l'ID : " + idUtilisateur);
        }
        agentStructureRepository.deleteById(idUtilisateur);
    }
}