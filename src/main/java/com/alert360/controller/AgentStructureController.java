package com.alert360.controller;


import com.alert360.controller.dto.AgentStructureRequestDto;
import com.alert360.controller.dto.AgentStructureResponseDto;
import com.alert360.service.serviceInter.AgentStructureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentStructureController {

    private final AgentStructureService agentStructureService;

    @PostMapping("/structure/{idStructure}")
    public ResponseEntity<AgentStructureResponseDto> creerAgent(
            @PathVariable Long idStructure,
            @Valid @RequestBody AgentStructureRequestDto dto) {
        AgentStructureResponseDto nouveauAgent = agentStructureService.creerAgent(dto, idStructure);
        return new ResponseEntity<>(nouveauAgent, HttpStatus.CREATED);
    }

    @PutMapping("/{idUtilisateur}")
    public ResponseEntity<AgentStructureResponseDto> modifierAgent(
            @PathVariable Long idUtilisateur,
            @Valid @RequestBody AgentStructureRequestDto dto) {
        AgentStructureResponseDto agentModifie = agentStructureService.modifierAgent(idUtilisateur, dto);
        return ResponseEntity.ok(agentModifie);
    }

    @GetMapping("/{idUtilisateur}")
    public ResponseEntity<AgentStructureResponseDto> obtenirParId(@PathVariable Long idUtilisateur) {
        AgentStructureResponseDto agent = agentStructureService.obtenirParId(idUtilisateur);
        return ResponseEntity.ok(agent);
    }


    @GetMapping
    public ResponseEntity<List<AgentStructureResponseDto>> obtenirTousLesAgents() {
        List<AgentStructureResponseDto> agents = agentStructureService.obtenirTousLesAgents();
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/structure/{idStructure}")
    public ResponseEntity<List<AgentStructureResponseDto>> obtenirAgentsParStructure(@PathVariable Long idStructure) {
        List<AgentStructureResponseDto> agents = agentStructureService.obtenirAgentsParStructure(idStructure);
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/matricule/{matricule}")
    public ResponseEntity<AgentStructureResponseDto> obtenirParMatricule(@PathVariable String matricule) {
        AgentStructureResponseDto agent = agentStructureService.obtenirParMatricule(matricule);
        return ResponseEntity.ok(agent);
    }

    @DeleteMapping("/{idUtilisateur}")
    public ResponseEntity<Void> supprimerAgent(@PathVariable Long idUtilisateur) {
        agentStructureService.supprimerAgent(idUtilisateur);
        return ResponseEntity.noContent().build();
    }
}
