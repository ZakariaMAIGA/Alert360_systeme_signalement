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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    @Override
    public AgentStructureResponseDto creerAgent(AgentStructureRequestDto dto, Long idStructure) {
        if (agentStructureRepository.existsByMatriculeAgent(dto.getMatriculeAgent())) {
            throw new RuntimeException("Un agent existe déjà avec le matricule : " + dto.getMatriculeAgent());
        }

        StructureCompetente structure = structureCompetenteRepository.findById(idStructure)
                .orElseThrow(() -> new RuntimeException("Structure compétente introuvable avec l'ID : " + idStructure));

        AgentStructure agent = requestMapper.toEntity(dto, structure);
        AgentStructure savedAgent = agentStructureRepository.save(agent);

        return responseMapper.toDto(savedAgent);
    }

    @Override
    public AgentStructureResponseDto modifierAgent(Long idUtilisateur, AgentStructureRequestDto dto) {
        AgentStructure agent = agentStructureRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Agent de structure introuvable avec l'ID : " + idUtilisateur));

        // Vérification de l'unicité du matricule en cas de modification
        if (!agent.getMatriculeAgent().equals(dto.getMatriculeAgent())
                && agentStructureRepository.existsByMatriculeAgent(dto.getMatriculeAgent())) {
            throw new RuntimeException("Un autre agent possède déjà le matricule : " + dto.getMatriculeAgent());
        }

        // Si la structure rattachée change
        if (dto.getIdStructure() != null && !dto.getIdStructure().equals(agent.getStructure().getIdStructure())) {
            StructureCompetente nouvelleStructure = structureCompetenteRepository.findById(dto.getIdStructure())
                    .orElseThrow(() -> new RuntimeException("Structure compétente introuvable avec l'ID : " + dto.getIdStructure()));
            agent.setStructure(nouvelleStructure);
        }

        agent.setNom(dto.getNom());
        agent.setPrenom(dto.getPrenom());
        agent.setTelephone(dto.getTelephone());
        agent.setEmail(dto.getEmail());
        agent.setMotDePasse(dto.getMotDePasse());
        agent.setMatriculeAgent(dto.getMatriculeAgent());
        if (dto.getEstResponsable() != null) {
            agent.setEstResponsable(dto.getEstResponsable());
        }

        AgentStructure updatedAgent = agentStructureRepository.save(agent);
        return responseMapper.toDto(updatedAgent);
    }

    @Override
    public AgentStructureResponseDto obtenirParId(Long idUtilisateur) {
        AgentStructure agent = agentStructureRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Agent de structure introuvable avec l'ID : " + idUtilisateur));
        return responseMapper.toDto(agent);
    }

    @Override
    public List<AgentStructureResponseDto> obtenirTousLesAgents() {
        return agentStructureRepository.findAll().stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AgentStructureResponseDto> obtenirAgentsParStructure(Long idStructure) {
        if (!structureCompetenteRepository.existsById(idStructure)) {
            throw new RuntimeException("Structure compétente introuvable avec l'ID : " + idStructure);
        }

        return agentStructureRepository.findByStructureIdStructure(idStructure).stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AgentStructureResponseDto obtenirParMatricule(String matricule) {
        AgentStructure agent = agentStructureRepository.findByMatriculeAgent(matricule)
                .orElseThrow(() -> new RuntimeException("Agent introuvable avec le matricule : " + matricule));
        return responseMapper.toDto(agent);
    }

    @Override
    public void supprimerAgent(Long idUtilisateur) {
        if (!agentStructureRepository.existsById(idUtilisateur)) {
            throw new RuntimeException("Agent de structure introuvable avec l'ID : " + idUtilisateur);
        }
        agentStructureRepository.deleteById(idUtilisateur);
    }
}
